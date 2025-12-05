package com.ailang.sample

import android.app.Application
import com.ailang.core.AiLang

/**
 * Application class for AiLang Sample App.
 * Demonstrates how to initialize AiLang SDK.
 */
class AiLangSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize AiLang with your Gemini API key
        // In production, store the API key securely (e.g., BuildConfig, encrypted storage)
        AiLang.initialize(
            context = this,
            apiKey = "YOUR_GEMINI_API_KEY", // Replace with your actual API key
            defaultLanguage = "en"
        )
    }
}
