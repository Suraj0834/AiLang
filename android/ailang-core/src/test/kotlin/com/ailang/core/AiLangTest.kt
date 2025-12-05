package com.ailang.core

import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test

/**
 * Unit tests for AiLang core functionality
 * Tests the main AiLang singleton methods in test mode
 */
class AiLangTest {

    @Before
    fun setup() {
        // Enable test mode before each test
        AiLang.setTestMode(true)
    }

    @After
    fun teardown() {
        // Cleanup after each test
        AiLang.setTestMode(false)
        AiLang.setMockTranslations(emptyMap())
    }

    // ==================== Basic Translation Tests ====================

    @Test
    fun `t() returns key when no translation found`() {
        // Given: Test mode with empty translations
        AiLang.setMockTranslations(emptyMap())

        // When: Translating a key
        val result = AiLang.t("unknown_key")

        // Then: Returns the key itself
        assertEquals("unknown_key", result)
    }

    @Test
    fun `t() returns translation when found`() {
        // Given: Mock translation
        AiLang.setMockTranslations(mapOf(
            "hello" to "Hola"
        ))

        // When: Translating the key
        val result = AiLang.t("hello")

        // Then: Returns the translated value
        assertEquals("Hola", result)
    }

    @Test
    fun `t() returns correct translation for multiple keys`() {
        // Given: Multiple mock translations
        AiLang.setMockTranslations(mapOf(
            "hello" to "Hola",
            "goodbye" to "Adiós",
            "thank_you" to "Gracias"
        ))

        // When/Then: All keys return correct translations
        assertEquals("Hola", AiLang.t("hello"))
        assertEquals("Adiós", AiLang.t("goodbye"))
        assertEquals("Gracias", AiLang.t("thank_you"))
    }

    // ==================== Parameterized Translation Tests ====================

    @Test
    fun `t() with params replaces single parameter`() {
        // Given: Translation with parameter
        AiLang.setMockTranslations(mapOf(
            "greeting" to "Hello, {name}!"
        ))

        // When: Translating with params
        val result = AiLang.t("greeting", mapOf("name" to "John"))

        // Then: Parameter is replaced
        assertEquals("Hello, John!", result)
    }

    @Test
    fun `t() with params replaces multiple parameters`() {
        // Given: Translation with multiple parameters
        AiLang.setMockTranslations(mapOf(
            "welcome" to "Welcome to {city}, {name}! Temperature: {temp}°C"
        ))

        // When: Translating with multiple params
        val result = AiLang.t("welcome", mapOf(
            "city" to "Paris",
            "name" to "Alice",
            "temp" to 25
        ))

        // Then: All parameters are replaced
        assertEquals("Welcome to Paris, Alice! Temperature: 25°C", result)
    }

    @Test
    fun `t() with params handles missing parameter`() {
        // Given: Translation with parameter
        AiLang.setMockTranslations(mapOf(
            "greeting" to "Hello, {name}!"
        ))

        // When: Translating without providing the param
        val result = AiLang.t("greeting", mapOf("other" to "value"))

        // Then: Parameter placeholder remains
        assertEquals("Hello, {name}!", result)
    }

    @Test
    fun `t() with params handles empty params map`() {
        // Given: Translation with parameter
        AiLang.setMockTranslations(mapOf(
            "greeting" to "Hello, {name}!"
        ))

        // When: Translating with empty params
        val result = AiLang.t("greeting", emptyMap())

        // Then: Parameter placeholder remains
        assertEquals("Hello, {name}!", result)
    }

    @Test
    fun `t() with params handles special characters in value`() {
        // Given: Translation with parameter
        AiLang.setMockTranslations(mapOf(
            "message" to "Value: {value}"
        ))

        // When: Translating with special characters
        val result = AiLang.t("message", mapOf("value" to "test@email.com"))

        // Then: Special characters are preserved
        assertEquals("Value: test@email.com", result)
    }

    // ==================== Count/Pluralization Tests ====================

    @Test
    fun `t() with count returns singular form for count 1`() {
        // Given: Translation with plural forms
        AiLang.setMockTranslations(mapOf(
            "items" to "{count} item|{count} items"
        ))

        // When: Translating with count 1
        val result = AiLang.t("items", 1)

        // Then: Singular form is used
        assertEquals("1 item", result)
    }

    @Test
    fun `t() with count returns plural form for count greater than 1`() {
        // Given: Translation with plural forms
        AiLang.setMockTranslations(mapOf(
            "items" to "{count} item|{count} items"
        ))

        // When: Translating with count > 1
        val result = AiLang.t("items", 5)

        // Then: Plural form is used
        assertEquals("5 items", result)
    }

    @Test
    fun `t() with count returns plural form for count 0`() {
        // Given: Translation with plural forms
        AiLang.setMockTranslations(mapOf(
            "items" to "{count} item|{count} items"
        ))

        // When: Translating with count 0
        val result = AiLang.t("items", 0)

        // Then: Plural form is used (0 items, not 0 item)
        assertEquals("0 items", result)
    }

    @Test
    fun `t() with count handles no pipe separator`() {
        // Given: Translation without plural forms
        AiLang.setMockTranslations(mapOf(
            "count_message" to "You have {count} notifications"
        ))

        // When: Translating with count
        val result = AiLang.t("count_message", 3)

        // Then: Count is replaced
        assertEquals("You have 3 notifications", result)
    }

    // ==================== Test Mode Tests ====================

    @Test
    fun `test mode can be toggled`() {
        // Given: Test mode is enabled
        AiLang.setTestMode(true)
        AiLang.setMockTranslations(mapOf("key" to "mock_value"))
        
        // When/Then: Mock translation is returned
        assertEquals("mock_value", AiLang.t("key"))
        
        // When: Test mode is disabled
        // Note: This would throw if not initialized, but test mode checks first
        AiLang.setTestMode(false)
    }

    @Test
    fun `mock translations can be updated`() {
        // Given: Initial mock translations
        AiLang.setMockTranslations(mapOf("key" to "value1"))
        assertEquals("value1", AiLang.t("key"))

        // When: Mock translations are updated
        AiLang.setMockTranslations(mapOf("key" to "value2"))

        // Then: New value is returned
        assertEquals("value2", AiLang.t("key"))
    }

    @Test
    fun `mock translations can be cleared`() {
        // Given: Mock translations
        AiLang.setMockTranslations(mapOf("key" to "value"))
        assertEquals("value", AiLang.t("key"))

        // When: Mock translations are cleared
        AiLang.setMockTranslations(emptyMap())

        // Then: Key is returned (fallback)
        assertEquals("key", AiLang.t("key"))
    }

    // ==================== Edge Cases ====================

    @Test
    fun `t() handles empty key`() {
        AiLang.setMockTranslations(mapOf("" to "empty key translation"))
        
        val result = AiLang.t("")
        
        assertEquals("empty key translation", result)
    }

    @Test
    fun `t() handles key with special characters`() {
        AiLang.setMockTranslations(mapOf(
            "key.with.dots" to "translation1",
            "key-with-dashes" to "translation2",
            "key_with_underscores" to "translation3"
        ))

        assertEquals("translation1", AiLang.t("key.with.dots"))
        assertEquals("translation2", AiLang.t("key-with-dashes"))
        assertEquals("translation3", AiLang.t("key_with_underscores"))
    }

    @Test
    fun `t() handles unicode in translation`() {
        AiLang.setMockTranslations(mapOf(
            "greeting" to "नमस्ते",
            "japanese" to "こんにちは",
            "arabic" to "مرحبا"
        ))

        assertEquals("नमस्ते", AiLang.t("greeting"))
        assertEquals("こんにちは", AiLang.t("japanese"))
        assertEquals("مرحبا", AiLang.t("arabic"))
    }

    @Test
    fun `t() handles very long translation`() {
        val longText = "A".repeat(10000)
        AiLang.setMockTranslations(mapOf("long" to longText))

        val result = AiLang.t("long")
        
        assertEquals(10000, result.length)
        assertEquals(longText, result)
    }

    @Test
    fun `t() handles newlines in translation`() {
        AiLang.setMockTranslations(mapOf(
            "multiline" to "Line 1\nLine 2\nLine 3"
        ))

        val result = AiLang.t("multiline")
        
        assertTrue(result.contains("\n"))
        assertEquals("Line 1\nLine 2\nLine 3", result)
    }

    @Test
    fun `t() with params handles integer values`() {
        AiLang.setMockTranslations(mapOf(
            "count" to "Count: {num}"
        ))

        val result = AiLang.t("count", mapOf("num" to 42))
        
        assertEquals("Count: 42", result)
    }

    @Test
    fun `t() with params handles float values`() {
        AiLang.setMockTranslations(mapOf(
            "price" to "Price: ${"{"}amount}"
        ))

        val result = AiLang.t("price", mapOf("amount" to 19.99))
        
        assertEquals("Price: 19.99", result)
    }

    @Test
    fun `t() with params handles boolean values`() {
        AiLang.setMockTranslations(mapOf(
            "status" to "Active: {active}"
        ))

        val result = AiLang.t("status", mapOf("active" to true))
        
        assertEquals("Active: true", result)
    }
}
