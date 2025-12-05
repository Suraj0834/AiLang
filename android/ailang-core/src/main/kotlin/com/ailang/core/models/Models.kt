package com.ailang.core.models

/**
 * Language data class
 * 
 * @property code ISO 639-1 language code (e.g., "en", "hi")
 * @property name English name of the language
 * @property nativeName Native name of the language
 */
data class Language(
    val code: String,
    val name: String,
    val nativeName: String
) {
    override fun toString(): String = "$name ($code)"
}

/**
 * Translation result data class
 * 
 * @property key Original translation key
 * @property originalText Original text (base language)
 * @property translatedText Translated text
 * @property targetLanguage Target language code
 * @property fromCache Whether this translation was from cache
 */
data class TranslationResult(
    val key: String,
    val originalText: String,
    val translatedText: String,
    val targetLanguage: String,
    val fromCache: Boolean = false
)

/**
 * Translation request data class
 * 
 * @property strings Map of keys to text strings to translate
 * @property targetLanguage Target language code
 */
data class TranslationRequest(
    val strings: Map<String, String>,
    val targetLanguage: String
)

/**
 * Translation error types
 */
sealed class TranslationError : Exception() {
    data class NetworkError(override val message: String) : TranslationError()
    data class ApiError(val code: Int, override val message: String) : TranslationError()
    data class RateLimitError(override val message: String) : TranslationError()
    data class ParseError(override val message: String) : TranslationError()
    data class ConfigError(override val message: String) : TranslationError()
}
