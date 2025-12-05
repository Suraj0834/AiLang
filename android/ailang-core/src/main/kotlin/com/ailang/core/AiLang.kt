package com.ailang.core

import android.content.Context
import com.ailang.core.cache.CacheManager
import com.ailang.core.config.AiLangConfig
import com.ailang.core.engine.TranslationEngine
import com.ailang.core.language.LanguageManager
import com.ailang.core.models.Language
import kotlinx.coroutines.*

/**
 * AiLang - AI-Powered Real-Time Translation Framework
 * 
 * Main entry point for the AiLang SDK. Use this singleton class to:
 * - Initialize the SDK with your API key
 * - Translate strings using t() method
 * - Change languages dynamically
 * - Configure caching and other options
 * 
 * @author Suraj
 * @version 1.0.0
 * 
 * Usage:
 * ```kotlin
 * // Initialize in Application class
 * AiLang.init(context, "YOUR_API_KEY")
 * 
 * // Translate strings
 * val text = AiLang.t("welcome_message")
 * 
 * // Change language
 * AiLang.setLanguage("hi")
 * ```
 */
object AiLang {
    
    private var isInitialized = false
    private var testMode = false
    private var mockTranslations: Map<String, String> = emptyMap()
    
    private lateinit var context: Context
    private lateinit var config: AiLangConfig
    private lateinit var translationEngine: TranslationEngine
    private lateinit var cacheManager: CacheManager
    private lateinit var languageManager: LanguageManager
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val languageChangeListeners = mutableListOf<(String) -> Unit>()
    
    // Current translations cache for quick access
    private var currentTranslations: MutableMap<String, String> = mutableMapOf()
    private var baseStrings: Map<String, String> = emptyMap()
    
    /**
     * Initialize the AiLang SDK
     * 
     * @param context Application context
     * @param apiKey Google Gemini API key
     * @param config Optional configuration options
     */
    @JvmStatic
    @JvmOverloads
    fun init(
        context: Context,
        apiKey: String,
        config: AiLangConfig = AiLangConfig()
    ) {
        if (isInitialized) {
            log("AiLang already initialized")
            return
        }
        
        this.context = context.applicationContext
        this.config = config
        
        // Initialize components
        cacheManager = CacheManager(this.context, config)
        languageManager = LanguageManager(this.context, config)
        translationEngine = TranslationEngine(apiKey, config)
        
        // Load base strings
        loadBaseStrings()
        
        // Load cached translations for current language
        loadCachedTranslations()
        
        isInitialized = true
        log("AiLang initialized successfully")
    }
    
    /**
     * Translate a string key to the current language
     * 
     * @param key The string key to translate
     * @return Translated string or the key if translation not found
     */
    @JvmStatic
    fun t(key: String): String {
        if (testMode) {
            return mockTranslations[key] ?: key
        }
        
        checkInitialized()
        
        // If current language is default, return base string
        if (languageManager.getCurrentLanguage() == config.defaultLanguage) {
            return baseStrings[key] ?: key
        }
        
        // Check current translations cache
        currentTranslations[key]?.let { return it }
        
        // Check persistent cache
        val cachedTranslation = cacheManager.get(key, languageManager.getCurrentLanguage())
        if (cachedTranslation != null) {
            currentTranslations[key] = cachedTranslation
            return cachedTranslation
        }
        
        // Translation not found, trigger async translation
        scope.launch {
            translateAsync(key)
        }
        
        // Return base string or key as fallback
        return baseStrings[key] ?: key
    }
    
    /**
     * Translate a string with parameters
     * 
     * @param key The string key to translate
     * @param params Map of parameter names to values
     * @return Translated string with parameters replaced
     * 
     * Example:
     * ```kotlin
     * // Base string: "Hello, {name}!"
     * val greeting = AiLang.t("greeting", mapOf("name" to "John"))
     * // Result: "Hello, John!"
     * ```
     */
    @JvmStatic
    fun t(key: String, params: Map<String, Any>): String {
        var result = t(key)
        params.forEach { (paramKey, value) ->
            result = result.replace("{$paramKey}", value.toString())
        }
        return result
    }
    
    /**
     * Translate a string with count for pluralization
     * 
     * @param key The string key to translate
     * @param count The count for pluralization
     * @return Translated string with proper plural form
     */
    @JvmStatic
    fun t(key: String, count: Int): String {
        val translated = t(key)
        val params = mapOf("count" to count)
        
        // Handle simple pluralization (singular|plural)
        if (translated.contains("|")) {
            val parts = translated.split("|")
            val result = if (count == 1) parts[0] else parts.getOrElse(1) { parts[0] }
            return result.replace("{count}", count.toString())
        }
        
        return t(key, params)
    }
    
    /**
     * Set the current language
     * 
     * @param languageCode ISO 639-1 language code (e.g., "en", "hi", "es")
     */
    @JvmStatic
    fun setLanguage(languageCode: String) {
        checkInitialized()
        
        if (!languageManager.isSupported(languageCode)) {
            log("Language $languageCode is not supported")
            return
        }
        
        val previousLanguage = languageManager.getCurrentLanguage()
        languageManager.setCurrentLanguage(languageCode)
        
        // Clear current translations and reload
        currentTranslations.clear()
        loadCachedTranslations()
        
        // Trigger async translation of base strings
        if (languageCode != config.defaultLanguage) {
            scope.launch {
                translateAllStrings()
            }
        }
        
        // Notify listeners
        if (previousLanguage != languageCode) {
            notifyLanguageChange(languageCode)
        }
        
        log("Language changed to: $languageCode")
    }
    
    /**
     * Get the current language code
     * 
     * @return Current ISO 639-1 language code
     */
    @JvmStatic
    fun getCurrentLanguage(): String {
        checkInitialized()
        return languageManager.getCurrentLanguage()
    }
    
    /**
     * Get list of all supported languages
     * 
     * @return List of Language objects with code and name
     */
    @JvmStatic
    fun getSupportedLanguages(): List<Language> {
        checkInitialized()
        return languageManager.getSupportedLanguages()
    }
    
    /**
     * Check if current language is RTL (Right-to-Left)
     * 
     * @return true if current language is RTL
     */
    @JvmStatic
    fun isRTL(): Boolean {
        checkInitialized()
        return languageManager.isRTL(languageManager.getCurrentLanguage())
    }
    
    /**
     * Preload translations for a specific language
     * 
     * @param languageCode ISO 639-1 language code
     * @param callback Optional callback when preloading is complete
     */
    @JvmStatic
    @JvmOverloads
    fun preloadLanguage(languageCode: String, callback: ((Boolean) -> Unit)? = null) {
        checkInitialized()
        
        scope.launch {
            try {
                val translations = translationEngine.translateBatch(
                    baseStrings,
                    languageCode
                )
                
                translations.forEach { (key, value) ->
                    cacheManager.put(key, value, languageCode)
                }
                
                callback?.invoke(true)
                log("Preloaded language: $languageCode")
            } catch (e: Exception) {
                log("Failed to preload language $languageCode: ${e.message}")
                callback?.invoke(false)
            }
        }
    }
    
    /**
     * Clear all cached translations
     */
    @JvmStatic
    fun clearCache() {
        checkInitialized()
        cacheManager.clearAll()
        currentTranslations.clear()
        log("Cache cleared")
    }
    
    /**
     * Add a listener for language changes
     * 
     * @param listener Callback invoked when language changes
     */
    @JvmStatic
    fun addLanguageChangeListener(listener: (String) -> Unit) {
        languageChangeListeners.add(listener)
    }
    
    /**
     * Remove a language change listener
     * 
     * @param listener The listener to remove
     */
    @JvmStatic
    fun removeLanguageChangeListener(listener: (String) -> Unit) {
        languageChangeListeners.remove(listener)
    }
    
    /**
     * Enable test mode for unit testing
     * 
     * @param enabled Whether test mode is enabled
     */
    @JvmStatic
    fun setTestMode(enabled: Boolean) {
        testMode = enabled
    }
    
    /**
     * Set mock translations for testing
     * 
     * @param translations Map of keys to translated values
     */
    @JvmStatic
    fun setMockTranslations(translations: Map<String, String>) {
        mockTranslations = translations
    }
    
    /**
     * Get current configuration
     * 
     * @return AiLangConfig object
     */
    @JvmStatic
    fun getConfig(): AiLangConfig {
        checkInitialized()
        return config
    }
    
    // ==================== Private Methods ====================
    
    private fun checkInitialized() {
        if (!isInitialized && !testMode) {
            throw IllegalStateException(
                "AiLang is not initialized. Call AiLang.init() first."
            )
        }
    }
    
    private fun loadBaseStrings() {
        try {
            // Load from assets/strings_en.json or config path
            val fileName = config.baseStringsPath ?: "strings_${config.defaultLanguage}.json"
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            baseStrings = com.google.gson.Gson().fromJson(
                json,
                object : com.google.gson.reflect.TypeToken<Map<String, String>>() {}.type
            )
            log("Loaded ${baseStrings.size} base strings")
        } catch (e: Exception) {
            log("Could not load base strings: ${e.message}")
            baseStrings = emptyMap()
        }
    }
    
    private fun loadCachedTranslations() {
        val currentLang = languageManager.getCurrentLanguage()
        if (currentLang == config.defaultLanguage) return
        
        baseStrings.keys.forEach { key ->
            cacheManager.get(key, currentLang)?.let { value ->
                currentTranslations[key] = value
            }
        }
        log("Loaded ${currentTranslations.size} cached translations")
    }
    
    private suspend fun translateAsync(key: String) {
        val currentLang = languageManager.getCurrentLanguage()
        val baseString = baseStrings[key] ?: return
        
        try {
            val translation = translationEngine.translate(baseString, currentLang)
            if (translation != null) {
                currentTranslations[key] = translation
                cacheManager.put(key, translation, currentLang)
            }
        } catch (e: Exception) {
            log("Translation failed for key '$key': ${e.message}")
        }
    }
    
    private suspend fun translateAllStrings() {
        val currentLang = languageManager.getCurrentLanguage()
        
        // Get untranslated strings
        val untranslated = baseStrings.filterKeys { key ->
            !currentTranslations.containsKey(key)
        }
        
        if (untranslated.isEmpty()) return
        
        try {
            val translations = translationEngine.translateBatch(untranslated, currentLang)
            translations.forEach { (key, value) ->
                currentTranslations[key] = value
                cacheManager.put(key, value, currentLang)
            }
            log("Translated ${translations.size} strings")
        } catch (e: Exception) {
            log("Batch translation failed: ${e.message}")
        }
    }
    
    private fun notifyLanguageChange(languageCode: String) {
        languageChangeListeners.forEach { listener ->
            try {
                listener(languageCode)
            } catch (e: Exception) {
                log("Language change listener error: ${e.message}")
            }
        }
    }
    
    private fun log(message: String) {
        if (::config.isInitialized && config.debugMode) {
            android.util.Log.d("AiLang", message)
        }
    }
}
