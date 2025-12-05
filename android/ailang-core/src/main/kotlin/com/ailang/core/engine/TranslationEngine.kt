package com.ailang.core.engine

import com.ailang.core.config.AiLangConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Translation Engine - Handles communication with Google Gemini AI API
 * 
 * This class is responsible for:
 * - Making API calls to Gemini for translations
 * - Batch processing multiple strings
 * - Retry logic with exponential backoff
 * - Rate limiting protection
 * 
 * @property apiKey Google Gemini API key
 * @property config AiLang configuration
 */
internal class TranslationEngine(
    private val apiKey: String,
    private val config: AiLangConfig
) {
    
    private val gson = Gson()
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(config.timeout.toLong(), TimeUnit.SECONDS)
        .readTimeout(config.timeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(config.timeout.toLong(), TimeUnit.SECONDS)
        .build()
    
    companion object {
        private const val GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
        private const val INITIAL_RETRY_DELAY = 1000L // 1 second
        private const val MAX_RETRY_DELAY = 30000L // 30 seconds
    }
    
    /**
     * Translate a single string to target language
     * 
     * @param text Text to translate
     * @param targetLanguage Target language code
     * @return Translated text or null on failure
     */
    suspend fun translate(text: String, targetLanguage: String): String? {
        return withContext(Dispatchers.IO) {
            val translations = translateBatch(mapOf("single" to text), targetLanguage)
            translations["single"]
        }
    }
    
    /**
     * Translate multiple strings in a single API call
     * 
     * @param strings Map of keys to text strings
     * @param targetLanguage Target language code
     * @return Map of keys to translated strings
     */
    suspend fun translateBatch(
        strings: Map<String, String>,
        targetLanguage: String
    ): Map<String, String> {
        return withContext(Dispatchers.IO) {
            val result = mutableMapOf<String, String>()
            
            // Split into batches
            val batches = strings.entries.chunked(config.batchSize)
            
            for (batch in batches) {
                val batchMap = batch.associate { it.key to it.value }
                val translated = translateBatchInternal(batchMap, targetLanguage)
                result.putAll(translated)
                
                // Small delay between batches to avoid rate limiting
                if (batches.size > 1) {
                    delay(100)
                }
            }
            
            result
        }
    }
    
    private suspend fun translateBatchInternal(
        strings: Map<String, String>,
        targetLanguage: String
    ): Map<String, String> {
        var lastException: Exception? = null
        var retryDelay = INITIAL_RETRY_DELAY
        
        repeat(config.retryCount) { attempt ->
            try {
                return makeTranslationRequest(strings, targetLanguage)
            } catch (e: RateLimitException) {
                // Wait longer for rate limit errors
                delay(retryDelay * 2)
                retryDelay = minOf(retryDelay * 2, MAX_RETRY_DELAY)
                lastException = e
            } catch (e: Exception) {
                delay(retryDelay)
                retryDelay = minOf(retryDelay * 2, MAX_RETRY_DELAY)
                lastException = e
            }
        }
        
        throw lastException ?: Exception("Translation failed after ${config.retryCount} retries")
    }
    
    private fun makeTranslationRequest(
        strings: Map<String, String>,
        targetLanguage: String
    ): Map<String, String> {
        val languageName = getLanguageName(targetLanguage)
        val stringsJson = gson.toJson(strings)
        
        val prompt = """
            You are a professional translator. Translate the following JSON object values from English to $languageName.
            
            IMPORTANT RULES:
            1. Only translate the VALUES, keep the KEYS exactly the same
            2. Maintain any placeholders like {name}, {count}, etc.
            3. Return ONLY valid JSON, no explanations
            4. Keep the same JSON structure
            5. Translate naturally, considering context
            6. If a value contains "|" for pluralization, translate both parts
            
            JSON to translate:
            $stringsJson
            
            Return only the translated JSON object:
        """.trimIndent()
        
        val requestBody = mapOf(
            "contents" to listOf(
                mapOf(
                    "parts" to listOf(
                        mapOf("text" to prompt)
                    )
                )
            ),
            "generationConfig" to mapOf(
                "temperature" to 0.3,
                "topP" to 0.95,
                "maxOutputTokens" to 8192
            )
        )
        
        val request = Request.Builder()
            .url("$GEMINI_API_URL?key=$apiKey")
            .post(gson.toJson(requestBody).toRequestBody("application/json".toMediaType()))
            .build()
        
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response")
        
        if (response.code == 429) {
            throw RateLimitException("Rate limit exceeded")
        }
        
        if (!response.isSuccessful) {
            throw Exception("API error: ${response.code} - $responseBody")
        }
        
        return parseResponse(responseBody)
    }
    
    private fun parseResponse(responseBody: String): Map<String, String> {
        try {
            val jsonResponse = gson.fromJson(responseBody, Map::class.java)
            val candidates = jsonResponse["candidates"] as? List<*>
            val firstCandidate = candidates?.firstOrNull() as? Map<*, *>
            val content = firstCandidate?.get("content") as? Map<*, *>
            val parts = content?.get("parts") as? List<*>
            val firstPart = parts?.firstOrNull() as? Map<*, *>
            var text = firstPart?.get("text") as? String ?: throw Exception("No text in response")
            
            // Clean up the response
            text = text.trim()
            if (text.startsWith("```json")) {
                text = text.removePrefix("```json").removeSuffix("```").trim()
            } else if (text.startsWith("```")) {
                text = text.removePrefix("```").removeSuffix("```").trim()
            }
            
            return gson.fromJson(
                text,
                object : TypeToken<Map<String, String>>() {}.type
            )
        } catch (e: Exception) {
            throw Exception("Failed to parse response: ${e.message}")
        }
    }
    
    private fun getLanguageName(code: String): String {
        return languageNames[code] ?: code
    }
    
    private val languageNames = mapOf(
        "en" to "English",
        "hi" to "Hindi",
        "es" to "Spanish",
        "fr" to "French",
        "de" to "German",
        "it" to "Italian",
        "pt" to "Portuguese",
        "ru" to "Russian",
        "ja" to "Japanese",
        "ko" to "Korean",
        "zh" to "Chinese (Simplified)",
        "ar" to "Arabic",
        "tr" to "Turkish",
        "nl" to "Dutch",
        "pl" to "Polish",
        "sv" to "Swedish",
        "th" to "Thai",
        "vi" to "Vietnamese",
        "id" to "Indonesian",
        "ms" to "Malay",
        "fil" to "Filipino",
        "bn" to "Bengali",
        "ta" to "Tamil",
        "te" to "Telugu",
        "mr" to "Marathi",
        "gu" to "Gujarati",
        "kn" to "Kannada",
        "ml" to "Malayalam",
        "pa" to "Punjabi",
        "ur" to "Urdu",
        "el" to "Greek",
        "cs" to "Czech",
        "ro" to "Romanian",
        "hu" to "Hungarian",
        "fi" to "Finnish",
        "no" to "Norwegian",
        "da" to "Danish",
        "uk" to "Ukrainian",
        "he" to "Hebrew",
        "fa" to "Persian",
        "sw" to "Swahili",
        "af" to "Afrikaans",
        "bg" to "Bulgarian",
        "ca" to "Catalan",
        "hr" to "Croatian",
        "et" to "Estonian",
        "lv" to "Latvian",
        "lt" to "Lithuanian",
        "sk" to "Slovak",
        "sl" to "Slovenian"
    )
    
    /**
     * Custom exception for rate limiting
     */
    class RateLimitException(message: String) : Exception(message)
}
