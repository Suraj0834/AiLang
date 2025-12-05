package com.ailang.core.language

import android.content.Context
import android.content.SharedPreferences
import com.ailang.core.config.AiLangConfig
import com.ailang.core.models.Language
import java.util.Locale

/**
 * Language Manager - Handles language selection and detection
 * 
 * Features:
 * - Store and retrieve current language preference
 * - Provide list of supported languages
 * - Detect device language
 * - RTL language detection
 * 
 * @property context Application context
 * @property config AiLang configuration
 */
internal class LanguageManager(
    private val context: Context,
    private val config: AiLangConfig
) {
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    companion object {
        private const val PREFS_NAME = "ailang_language"
        private const val KEY_CURRENT_LANGUAGE = "current_language"
        
        // RTL language codes
        private val RTL_LANGUAGES = setOf(
            "ar", "he", "fa", "ur", "yi", "ps", "sd", "ug"
        )
    }
    
    /**
     * Get current language code
     * 
     * @return Current language code or default language
     */
    fun getCurrentLanguage(): String {
        return prefs.getString(KEY_CURRENT_LANGUAGE, null)
            ?: detectDeviceLanguage()
            ?: config.defaultLanguage
    }
    
    /**
     * Set current language
     * 
     * @param languageCode ISO 639-1 language code
     */
    fun setCurrentLanguage(languageCode: String) {
        prefs.edit().putString(KEY_CURRENT_LANGUAGE, languageCode).apply()
    }
    
    /**
     * Check if a language is supported
     * 
     * @param languageCode Language code to check
     * @return true if supported
     */
    fun isSupported(languageCode: String): Boolean {
        return supportedLanguages.any { it.code == languageCode }
    }
    
    /**
     * Get list of all supported languages
     * 
     * @return List of Language objects
     */
    fun getSupportedLanguages(): List<Language> {
        return supportedLanguages
    }
    
    /**
     * Check if a language is RTL (Right-to-Left)
     * 
     * @param languageCode Language code to check
     * @return true if RTL
     */
    fun isRTL(languageCode: String): Boolean {
        return RTL_LANGUAGES.contains(languageCode)
    }
    
    /**
     * Detect device's current language
     * 
     * @return Device language code or null if not supported
     */
    fun detectDeviceLanguage(): String? {
        val locale = Locale.getDefault()
        val deviceLang = locale.language
        
        return if (isSupported(deviceLang)) deviceLang else null
    }
    
    /**
     * Get language display name
     * 
     * @param languageCode Language code
     * @return Display name of the language
     */
    fun getLanguageName(languageCode: String): String {
        return supportedLanguages.find { it.code == languageCode }?.name
            ?: languageCode
    }
    
    /**
     * Get native language name
     * 
     * @param languageCode Language code
     * @return Native name of the language (e.g., "हिन्दी" for Hindi)
     */
    fun getNativeName(languageCode: String): String {
        return supportedLanguages.find { it.code == languageCode }?.nativeName
            ?: languageCode
    }
    
    /**
     * All supported languages
     */
    private val supportedLanguages = listOf(
        Language("en", "English", "English"),
        Language("hi", "Hindi", "हिन्दी"),
        Language("es", "Spanish", "Español"),
        Language("fr", "French", "Français"),
        Language("de", "German", "Deutsch"),
        Language("it", "Italian", "Italiano"),
        Language("pt", "Portuguese", "Português"),
        Language("ru", "Russian", "Русский"),
        Language("ja", "Japanese", "日本語"),
        Language("ko", "Korean", "한국어"),
        Language("zh", "Chinese", "中文"),
        Language("ar", "Arabic", "العربية"),
        Language("tr", "Turkish", "Türkçe"),
        Language("nl", "Dutch", "Nederlands"),
        Language("pl", "Polish", "Polski"),
        Language("sv", "Swedish", "Svenska"),
        Language("th", "Thai", "ไทย"),
        Language("vi", "Vietnamese", "Tiếng Việt"),
        Language("id", "Indonesian", "Bahasa Indonesia"),
        Language("ms", "Malay", "Bahasa Melayu"),
        Language("fil", "Filipino", "Filipino"),
        Language("bn", "Bengali", "বাংলা"),
        Language("ta", "Tamil", "தமிழ்"),
        Language("te", "Telugu", "తెలుగు"),
        Language("mr", "Marathi", "मराठी"),
        Language("gu", "Gujarati", "ગુજરાતી"),
        Language("kn", "Kannada", "ಕನ್ನಡ"),
        Language("ml", "Malayalam", "മലയാളം"),
        Language("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        Language("ur", "Urdu", "اردو"),
        Language("el", "Greek", "Ελληνικά"),
        Language("cs", "Czech", "Čeština"),
        Language("ro", "Romanian", "Română"),
        Language("hu", "Hungarian", "Magyar"),
        Language("fi", "Finnish", "Suomi"),
        Language("no", "Norwegian", "Norsk"),
        Language("da", "Danish", "Dansk"),
        Language("uk", "Ukrainian", "Українська"),
        Language("he", "Hebrew", "עברית"),
        Language("fa", "Persian", "فارسی"),
        Language("sw", "Swahili", "Kiswahili"),
        Language("af", "Afrikaans", "Afrikaans"),
        Language("bg", "Bulgarian", "Български"),
        Language("ca", "Catalan", "Català"),
        Language("hr", "Croatian", "Hrvatski"),
        Language("et", "Estonian", "Eesti"),
        Language("lv", "Latvian", "Latviešu"),
        Language("lt", "Lithuanian", "Lietuvių"),
        Language("sk", "Slovak", "Slovenčina"),
        Language("sl", "Slovenian", "Slovenščina")
    )
}
