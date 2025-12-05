package com.ailang.core.config

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for AiLangConfig
 */
class AiLangConfigTest {

    // ==================== Default Values Tests ====================

    @Test
    fun `default config has expected values`() {
        val config = AiLangConfig()

        assertEquals("en", config.defaultLanguage)
        assertTrue(config.cacheEnabled)
        assertEquals(24 * 60 * 60 * 1000L, config.cacheDuration)
        assertEquals(10, config.maxCacheSize)
        assertFalse(config.debugMode)
        assertEquals(50, config.batchSize)
        assertEquals(30, config.timeout)
        assertEquals(3, config.retryCount)
        assertNull(config.baseStringsPath)
    }

    // ==================== Constructor Tests ====================

    @Test
    fun `config with custom values`() {
        val config = AiLangConfig(
            defaultLanguage = "hi",
            cacheEnabled = false,
            cacheDuration = 1000L,
            maxCacheSize = 50,
            debugMode = true,
            batchSize = 100,
            timeout = 60,
            retryCount = 5,
            baseStringsPath = "custom/path.json"
        )

        assertEquals("hi", config.defaultLanguage)
        assertFalse(config.cacheEnabled)
        assertEquals(1000L, config.cacheDuration)
        assertEquals(50, config.maxCacheSize)
        assertTrue(config.debugMode)
        assertEquals(100, config.batchSize)
        assertEquals(60, config.timeout)
        assertEquals(5, config.retryCount)
        assertEquals("custom/path.json", config.baseStringsPath)
    }

    @Test
    fun `config with partial custom values`() {
        val config = AiLangConfig(
            defaultLanguage = "es",
            debugMode = true
        )

        assertEquals("es", config.defaultLanguage)
        assertTrue(config.cacheEnabled) // default
        assertTrue(config.debugMode)
        assertEquals(30, config.timeout) // default
    }

    // ==================== Builder Tests ====================

    @Test
    fun `builder creates config with default values`() {
        val config = AiLangConfig.builder().build()

        assertEquals("en", config.defaultLanguage)
        assertTrue(config.cacheEnabled)
        assertFalse(config.debugMode)
    }

    @Test
    fun `builder creates config with custom values`() {
        val config = AiLangConfig.builder()
            .defaultLanguage("fr")
            .cacheEnabled(false)
            .cacheDuration(5000L)
            .maxCacheSize(20)
            .debugMode(true)
            .batchSize(25)
            .timeout(45)
            .retryCount(2)
            .baseStringsPath("strings.json")
            .build()

        assertEquals("fr", config.defaultLanguage)
        assertFalse(config.cacheEnabled)
        assertEquals(5000L, config.cacheDuration)
        assertEquals(20, config.maxCacheSize)
        assertTrue(config.debugMode)
        assertEquals(25, config.batchSize)
        assertEquals(45, config.timeout)
        assertEquals(2, config.retryCount)
        assertEquals("strings.json", config.baseStringsPath)
    }

    @Test
    fun `builder allows chaining`() {
        val builder = AiLangConfig.builder()
            .defaultLanguage("de")
            .debugMode(true)
            .timeout(60)

        // Builder should return itself for chaining
        assertNotNull(builder)
        
        val config = builder.build()
        assertEquals("de", config.defaultLanguage)
        assertTrue(config.debugMode)
        assertEquals(60, config.timeout)
    }

    @Test
    fun `builder can be used with companion function`() {
        val config = AiLangConfig.builder()
            .defaultLanguage("ja")
            .build()

        assertEquals("ja", config.defaultLanguage)
    }

    // ==================== Copy Tests ====================

    @Test
    fun `copy creates new config with modified values`() {
        val original = AiLangConfig(defaultLanguage = "en", debugMode = false)
        val copy = original.copy(debugMode = true)

        assertEquals("en", original.defaultLanguage)
        assertFalse(original.debugMode)
        
        assertEquals("en", copy.defaultLanguage)
        assertTrue(copy.debugMode)
    }

    @Test
    fun `copy preserves unmodified values`() {
        val original = AiLangConfig(
            defaultLanguage = "en",
            cacheEnabled = true,
            cacheDuration = 5000L,
            maxCacheSize = 20
        )
        
        val copy = original.copy(defaultLanguage = "es")

        assertEquals("es", copy.defaultLanguage)
        assertTrue(copy.cacheEnabled)
        assertEquals(5000L, copy.cacheDuration)
        assertEquals(20, copy.maxCacheSize)
    }

    // ==================== Equality Tests ====================

    @Test
    fun `equal configs are equal`() {
        val config1 = AiLangConfig(defaultLanguage = "en", debugMode = true)
        val config2 = AiLangConfig(defaultLanguage = "en", debugMode = true)

        assertEquals(config1, config2)
        assertEquals(config1.hashCode(), config2.hashCode())
    }

    @Test
    fun `different configs are not equal`() {
        val config1 = AiLangConfig(defaultLanguage = "en")
        val config2 = AiLangConfig(defaultLanguage = "es")

        assertNotEquals(config1, config2)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `config with zero timeout`() {
        val config = AiLangConfig(timeout = 0)
        assertEquals(0, config.timeout)
    }

    @Test
    fun `config with zero cache duration`() {
        val config = AiLangConfig(cacheDuration = 0L)
        assertEquals(0L, config.cacheDuration)
    }

    @Test
    fun `config with zero batch size`() {
        val config = AiLangConfig(batchSize = 0)
        assertEquals(0, config.batchSize)
    }

    @Test
    fun `config with zero retry count`() {
        val config = AiLangConfig(retryCount = 0)
        assertEquals(0, config.retryCount)
    }

    @Test
    fun `config with empty default language`() {
        val config = AiLangConfig(defaultLanguage = "")
        assertEquals("", config.defaultLanguage)
    }

    @Test
    fun `config with very long base strings path`() {
        val longPath = "a/".repeat(100) + "strings.json"
        val config = AiLangConfig(baseStringsPath = longPath)
        assertEquals(longPath, config.baseStringsPath)
    }

    @Test
    fun `config with negative values`() {
        // While not recommended, should not throw
        val config = AiLangConfig(
            timeout = -1,
            retryCount = -5,
            maxCacheSize = -10
        )

        assertEquals(-1, config.timeout)
        assertEquals(-5, config.retryCount)
        assertEquals(-10, config.maxCacheSize)
    }

    @Test
    fun `config with large values`() {
        val config = AiLangConfig(
            cacheDuration = Long.MAX_VALUE,
            maxCacheSize = Int.MAX_VALUE,
            batchSize = Int.MAX_VALUE
        )

        assertEquals(Long.MAX_VALUE, config.cacheDuration)
        assertEquals(Int.MAX_VALUE, config.maxCacheSize)
        assertEquals(Int.MAX_VALUE, config.batchSize)
    }
}
