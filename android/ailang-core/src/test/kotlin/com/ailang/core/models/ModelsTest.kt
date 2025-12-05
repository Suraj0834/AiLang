package com.ailang.core.models

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for AiLang models
 */
class ModelsTest {

    // ==================== Language Tests ====================

    @Test
    fun `Language creation with all properties`() {
        val language = Language(
            code = "en",
            name = "English",
            nativeName = "English"
        )

        assertEquals("en", language.code)
        assertEquals("English", language.name)
        assertEquals("English", language.nativeName)
    }

    @Test
    fun `Language toString returns formatted string`() {
        val language = Language(
            code = "hi",
            name = "Hindi",
            nativeName = "हिन्दी"
        )

        assertEquals("Hindi (hi)", language.toString())
    }

    @Test
    fun `Language equality works correctly`() {
        val lang1 = Language("en", "English", "English")
        val lang2 = Language("en", "English", "English")
        val lang3 = Language("es", "Spanish", "Español")

        assertEquals(lang1, lang2)
        assertNotEquals(lang1, lang3)
    }

    @Test
    fun `Language copy creates new instance`() {
        val original = Language("en", "English", "English")
        val copy = original.copy(code = "es", name = "Spanish", nativeName = "Español")

        assertNotEquals(original, copy)
        assertEquals("es", copy.code)
        assertEquals("Spanish", copy.name)
    }

    // ==================== TranslationResult Tests ====================

    @Test
    fun `TranslationResult creation with all properties`() {
        val result = TranslationResult(
            key = "hello",
            originalText = "Hello",
            translatedText = "Hola",
            targetLanguage = "es",
            fromCache = true
        )

        assertEquals("hello", result.key)
        assertEquals("Hello", result.originalText)
        assertEquals("Hola", result.translatedText)
        assertEquals("es", result.targetLanguage)
        assertTrue(result.fromCache)
    }

    @Test
    fun `TranslationResult default fromCache is false`() {
        val result = TranslationResult(
            key = "hello",
            originalText = "Hello",
            translatedText = "Hola",
            targetLanguage = "es"
        )

        assertFalse(result.fromCache)
    }

    @Test
    fun `TranslationResult equality works correctly`() {
        val result1 = TranslationResult("k", "o", "t", "en", false)
        val result2 = TranslationResult("k", "o", "t", "en", false)
        val result3 = TranslationResult("k", "o", "t", "en", true)

        assertEquals(result1, result2)
        assertNotEquals(result1, result3)
    }

    // ==================== TranslationRequest Tests ====================

    @Test
    fun `TranslationRequest creation`() {
        val strings = mapOf(
            "hello" to "Hello",
            "goodbye" to "Goodbye"
        )
        val request = TranslationRequest(
            strings = strings,
            targetLanguage = "es"
        )

        assertEquals(2, request.strings.size)
        assertEquals("Hello", request.strings["hello"])
        assertEquals("es", request.targetLanguage)
    }

    @Test
    fun `TranslationRequest with empty strings`() {
        val request = TranslationRequest(
            strings = emptyMap(),
            targetLanguage = "fr"
        )

        assertTrue(request.strings.isEmpty())
    }

    // ==================== TranslationError Tests ====================

    @Test
    fun `NetworkError has correct message`() {
        val error = TranslationError.NetworkError("Connection failed")

        assertEquals("Connection failed", error.message)
        assertTrue(error is TranslationError)
        assertTrue(error is Exception)
    }

    @Test
    fun `ApiError has code and message`() {
        val error = TranslationError.ApiError(429, "Rate limit exceeded")

        assertEquals(429, error.code)
        assertEquals("Rate limit exceeded", error.message)
    }

    @Test
    fun `RateLimitError is TranslationError`() {
        val error = TranslationError.RateLimitError("Too many requests")

        assertTrue(error is TranslationError)
        assertEquals("Too many requests", error.message)
    }

    @Test
    fun `ParseError is TranslationError`() {
        val error = TranslationError.ParseError("Invalid JSON response")

        assertTrue(error is TranslationError)
        assertEquals("Invalid JSON response", error.message)
    }

    @Test
    fun `ConfigError is TranslationError`() {
        val error = TranslationError.ConfigError("Missing API key")

        assertTrue(error is TranslationError)
        assertEquals("Missing API key", error.message)
    }

    @Test
    fun `TranslationError sealed class pattern matching`() {
        val errors = listOf<TranslationError>(
            TranslationError.NetworkError("Net error"),
            TranslationError.ApiError(500, "Server error"),
            TranslationError.RateLimitError("Rate limit"),
            TranslationError.ParseError("Parse error"),
            TranslationError.ConfigError("Config error")
        )

        val errorTypes = errors.map { error ->
            when (error) {
                is TranslationError.NetworkError -> "network"
                is TranslationError.ApiError -> "api"
                is TranslationError.RateLimitError -> "rate"
                is TranslationError.ParseError -> "parse"
                is TranslationError.ConfigError -> "config"
            }
        }

        assertEquals(listOf("network", "api", "rate", "parse", "config"), errorTypes)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `Language with empty strings`() {
        val language = Language("", "", "")

        assertEquals("", language.code)
        assertEquals(" ()", language.toString())
    }

    @Test
    fun `Language with unicode characters`() {
        val language = Language(
            code = "ja",
            name = "Japanese",
            nativeName = "日本語"
        )

        assertEquals("日本語", language.nativeName)
    }

    @Test
    fun `TranslationResult with unicode content`() {
        val result = TranslationResult(
            key = "greeting",
            originalText = "Hello",
            translatedText = "नमस्ते",
            targetLanguage = "hi"
        )

        assertEquals("नमस्ते", result.translatedText)
    }

    @Test
    fun `TranslationRequest preserves order`() {
        val strings = linkedMapOf(
            "a" to "A",
            "b" to "B",
            "c" to "C"
        )
        val request = TranslationRequest(strings, "es")

        val keys = request.strings.keys.toList()
        assertEquals(listOf("a", "b", "c"), keys)
    }
}
