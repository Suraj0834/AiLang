package com.ailang.core.cache

import android.content.Context
import android.content.SharedPreferences
import com.ailang.core.config.AiLangConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for CacheManager
 */
@RunWith(MockitoJUnitRunner::class)
class CacheManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var config: AiLangConfig
    private lateinit var cacheManager: CacheManager

    @Before
    fun setup() {
        config = AiLangConfig(
            cacheEnabled = true,
            cacheDuration = 24 * 60 * 60 * 1000L, // 24 hours
            maxCacheSize = 10
        )

        // Mock SharedPreferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)
        `when`(editor.clear()).thenReturn(editor)
        `when`(sharedPreferences.getString(anyString(), isNull())).thenReturn(null)

        cacheManager = CacheManager(context, config)
    }

    // ==================== put() Tests ====================

    @Test
    fun `put stores value in cache`() {
        cacheManager.put("hello", "Hola", "es")

        // Verify the value can be retrieved
        val result = cacheManager.get("hello", "es")
        assertEquals("Hola", result)
    }

    @Test
    fun `put stores multiple values`() {
        cacheManager.put("hello", "Hola", "es")
        cacheManager.put("goodbye", "Adiós", "es")
        cacheManager.put("hello", "Bonjour", "fr")

        assertEquals("Hola", cacheManager.get("hello", "es"))
        assertEquals("Adiós", cacheManager.get("goodbye", "es"))
        assertEquals("Bonjour", cacheManager.get("hello", "fr"))
    }

    @Test
    fun `put overwrites existing value`() {
        cacheManager.put("hello", "Hola", "es")
        cacheManager.put("hello", "Hola Updated", "es")

        assertEquals("Hola Updated", cacheManager.get("hello", "es"))
    }

    // ==================== get() Tests ====================

    @Test
    fun `get returns null for non-existent key`() {
        val result = cacheManager.get("nonexistent", "es")
        
        assertNull(result)
    }

    @Test
    fun `get returns null for non-existent language`() {
        cacheManager.put("hello", "Hola", "es")
        
        val result = cacheManager.get("hello", "fr")
        
        assertNull(result)
    }

    @Test
    fun `get returns cached value`() {
        cacheManager.put("greeting", "Namaste", "hi")
        
        val result = cacheManager.get("greeting", "hi")
        
        assertEquals("Namaste", result)
    }

    // ==================== Cache Disabled Tests ====================

    @Test
    fun `put does nothing when cache disabled`() {
        val disabledConfig = AiLangConfig(cacheEnabled = false)
        val disabledCacheManager = CacheManager(context, disabledConfig)

        disabledCacheManager.put("key", "value", "es")
        
        val result = disabledCacheManager.get("key", "es")
        assertNull(result)
    }

    @Test
    fun `get returns null when cache disabled`() {
        val disabledConfig = AiLangConfig(cacheEnabled = false)
        val disabledCacheManager = CacheManager(context, disabledConfig)

        // Try to get even without putting
        val result = disabledCacheManager.get("key", "es")
        
        assertNull(result)
    }

    // ==================== remove() Tests ====================

    @Test
    fun `remove deletes cached entry`() {
        cacheManager.put("hello", "Hola", "es")
        assertEquals("Hola", cacheManager.get("hello", "es"))

        cacheManager.remove("hello", "es")
        
        assertNull(cacheManager.get("hello", "es"))
    }

    @Test
    fun `remove does not affect other languages`() {
        cacheManager.put("hello", "Hola", "es")
        cacheManager.put("hello", "Bonjour", "fr")

        cacheManager.remove("hello", "es")
        
        assertNull(cacheManager.get("hello", "es"))
        assertEquals("Bonjour", cacheManager.get("hello", "fr"))
    }

    @Test
    fun `remove non-existent key does not throw`() {
        // Should not throw
        cacheManager.remove("nonexistent", "es")
    }

    // ==================== clearAll() Tests ====================

    @Test
    fun `clearAll removes all entries`() {
        cacheManager.put("key1", "value1", "es")
        cacheManager.put("key2", "value2", "es")
        cacheManager.put("key3", "value3", "fr")

        cacheManager.clearAll()
        
        assertNull(cacheManager.get("key1", "es"))
        assertNull(cacheManager.get("key2", "es"))
        assertNull(cacheManager.get("key3", "fr"))
    }

    @Test
    fun `clearAll clears SharedPreferences`() {
        cacheManager.put("key", "value", "es")
        
        cacheManager.clearAll()
        
        verify(editor).clear()
        verify(editor, atLeastOnce()).apply()
    }

    // ==================== clearLanguage() Tests ====================

    @Test
    fun `clearLanguage removes only specified language entries`() {
        cacheManager.put("hello", "Hola", "es")
        cacheManager.put("goodbye", "Adiós", "es")
        cacheManager.put("hello", "Bonjour", "fr")

        cacheManager.clearLanguage("es")
        
        assertNull(cacheManager.get("hello", "es"))
        assertNull(cacheManager.get("goodbye", "es"))
        assertEquals("Bonjour", cacheManager.get("hello", "fr"))
    }

    @Test
    fun `clearLanguage with non-existent language does not throw`() {
        cacheManager.put("hello", "Hola", "es")
        
        // Should not throw
        cacheManager.clearLanguage("nonexistent")
        
        // Original entry should remain
        assertEquals("Hola", cacheManager.get("hello", "es"))
    }

    // ==================== getStats() Tests ====================

    @Test
    fun `getStats returns zero for empty cache`() {
        val stats = cacheManager.getStats()

        assertEquals(0, stats.totalEntries)
        assertEquals(0, stats.activeEntries)
        assertEquals(0, stats.expiredEntries)
    }

    @Test
    fun `getStats returns correct count after adding entries`() {
        cacheManager.put("key1", "value1", "es")
        cacheManager.put("key2", "value2", "es")
        cacheManager.put("key3", "value3", "fr")

        val stats = cacheManager.getStats()

        assertEquals(3, stats.totalEntries)
        assertEquals(3, stats.activeEntries)
        assertEquals(0, stats.expiredEntries)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `cache handles empty key`() {
        cacheManager.put("", "empty key value", "es")
        
        assertEquals("empty key value", cacheManager.get("", "es"))
    }

    @Test
    fun `cache handles empty value`() {
        cacheManager.put("key", "", "es")
        
        assertEquals("", cacheManager.get("key", "es"))
    }

    @Test
    fun `cache handles unicode content`() {
        cacheManager.put("greeting", "नमस्ते", "hi")
        cacheManager.put("japanese", "こんにちは", "ja")
        
        assertEquals("नमस्ते", cacheManager.get("greeting", "hi"))
        assertEquals("こんにちは", cacheManager.get("japanese", "ja"))
    }

    @Test
    fun `cache handles very long values`() {
        val longValue = "A".repeat(10000)
        cacheManager.put("long", longValue, "es")
        
        assertEquals(longValue, cacheManager.get("long", "es"))
    }

    @Test
    fun `cache handles special characters in key`() {
        cacheManager.put("key.with.dots", "value1", "es")
        cacheManager.put("key-with-dashes", "value2", "es")
        cacheManager.put("key_with_underscores", "value3", "es")
        
        assertEquals("value1", cacheManager.get("key.with.dots", "es"))
        assertEquals("value2", cacheManager.get("key-with-dashes", "es"))
        assertEquals("value3", cacheManager.get("key_with_underscores", "es"))
    }

    @Test
    fun `cache handles newlines in value`() {
        val multilineValue = "Line 1\nLine 2\nLine 3"
        cacheManager.put("multiline", multilineValue, "es")
        
        assertEquals(multilineValue, cacheManager.get("multiline", "es"))
    }

    @Test
    fun `same key different languages are separate`() {
        cacheManager.put("hello", "Hola", "es")
        cacheManager.put("hello", "Bonjour", "fr")
        cacheManager.put("hello", "Ciao", "it")

        assertEquals("Hola", cacheManager.get("hello", "es"))
        assertEquals("Bonjour", cacheManager.get("hello", "fr"))
        assertEquals("Ciao", cacheManager.get("hello", "it"))
    }
}
