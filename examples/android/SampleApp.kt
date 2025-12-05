package com.ailang.sample

import android.app.Application
import com.ailang.core.AiLang
import com.ailang.core.config.AiLangConfig

/**
 * Sample Application class demonstrating AiLang initialization
 */
class SampleApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize AiLang with configuration
        AiLang.init(
            context = this,
            apiKey = BuildConfig.GEMINI_API_KEY, // From local.properties
            config = AiLangConfig(
                defaultLanguage = "en",
                cacheEnabled = true,
                cacheDuration = 24 * 60 * 60 * 1000L, // 24 hours
                maxCacheSize = 10, // 10 MB
                debugMode = BuildConfig.DEBUG,
                batchSize = 50,
                timeout = 30,
                retryCount = 3,
                baseStringsPath = "strings_en.json" // In assets folder
            )
        )
        
        // Alternative: Using Builder pattern (Java-friendly)
        /*
        AiLang.init(
            context = this,
            apiKey = BuildConfig.GEMINI_API_KEY,
            config = AiLangConfig.builder()
                .defaultLanguage("en")
                .cacheEnabled(true)
                .cacheDuration(86400000L)
                .maxCacheSize(10)
                .debugMode(BuildConfig.DEBUG)
                .build()
        )
        */
    }
}
