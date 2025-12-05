package com.ailang.core.config

/**
 * Configuration options for AiLang SDK
 * 
 * @property defaultLanguage Default/fallback language code (e.g., "en")
 * @property cacheEnabled Whether to enable caching of translations
 * @property cacheDuration Duration to keep cached translations (milliseconds)
 * @property maxCacheSize Maximum cache size in megabytes
 * @property debugMode Enable debug logging
 * @property batchSize Number of strings to translate in a single API call
 * @property timeout Request timeout in seconds
 * @property retryCount Number of retries on failure
 * @property baseStringsPath Path to base strings JSON file in assets
 */
data class AiLangConfig(
    val defaultLanguage: String = "en",
    val cacheEnabled: Boolean = true,
    val cacheDuration: Long = 24 * 60 * 60 * 1000L, // 24 hours
    val maxCacheSize: Int = 10, // MB
    val debugMode: Boolean = false,
    val batchSize: Int = 50,
    val timeout: Int = 30, // seconds
    val retryCount: Int = 3,
    val baseStringsPath: String? = null
) {
    
    /**
     * Builder pattern for Java interoperability
     */
    class Builder {
        private var defaultLanguage: String = "en"
        private var cacheEnabled: Boolean = true
        private var cacheDuration: Long = 24 * 60 * 60 * 1000L
        private var maxCacheSize: Int = 10
        private var debugMode: Boolean = false
        private var batchSize: Int = 50
        private var timeout: Int = 30
        private var retryCount: Int = 3
        private var baseStringsPath: String? = null
        
        fun defaultLanguage(lang: String) = apply { defaultLanguage = lang }
        fun cacheEnabled(enabled: Boolean) = apply { cacheEnabled = enabled }
        fun cacheDuration(duration: Long) = apply { cacheDuration = duration }
        fun maxCacheSize(size: Int) = apply { maxCacheSize = size }
        fun debugMode(enabled: Boolean) = apply { debugMode = enabled }
        fun batchSize(size: Int) = apply { batchSize = size }
        fun timeout(seconds: Int) = apply { timeout = seconds }
        fun retryCount(count: Int) = apply { retryCount = count }
        fun baseStringsPath(path: String?) = apply { baseStringsPath = path }
        
        fun build() = AiLangConfig(
            defaultLanguage = defaultLanguage,
            cacheEnabled = cacheEnabled,
            cacheDuration = cacheDuration,
            maxCacheSize = maxCacheSize,
            debugMode = debugMode,
            batchSize = batchSize,
            timeout = timeout,
            retryCount = retryCount,
            baseStringsPath = baseStringsPath
        )
    }
    
    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}
