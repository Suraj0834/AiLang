package com.ailang.core.language

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
 * Unit tests for LanguageManager
 */
@RunWith(MockitoJUnitRunner::class)
class LanguageManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var config: AiLangConfig
    private lateinit var languageManager: LanguageManager

    @Before
    fun setup() {
        config = AiLangConfig(defaultLanguage = "en")

        // Mock SharedPreferences
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        languageManager = LanguageManager(context, config)
    }

    // ==================== getCurrentLanguage Tests ====================

    @Test
    fun `getCurrentLanguage returns stored language`() {
        `when`(sharedPreferences.getString(eq("current_language"), isNull())).thenReturn("hi")

        val result = languageManager.getCurrentLanguage()

        assertEquals("hi", result)
    }

    @Test
    fun `getCurrentLanguage returns default when no preference set`() {
        `when`(sharedPreferences.getString(eq("current_language"), isNull())).thenReturn(null)

        val result = languageManager.getCurrentLanguage()

        // Should return default or detected device language
        assertNotNull(result)
    }

    // ==================== setCurrentLanguage Tests ====================

    @Test
    fun `setCurrentLanguage stores preference`() {
        languageManager.setCurrentLanguage("es")

        verify(editor).putString("current_language", "es")
        verify(editor).apply()
    }

    @Test
    fun `setCurrentLanguage with different languages`() {
        val languages = listOf("en", "hi", "ja", "ar", "zh")
        
        languages.forEach { lang ->
            languageManager.setCurrentLanguage(lang)
            verify(editor).putString("current_language", lang)
        }
    }

    // ==================== isSupported Tests ====================

    @Test
    fun `isSupported returns true for supported languages`() {
        val supportedCodes = listOf("en", "hi", "es", "fr", "de", "ja", "ar", "zh")
        
        supportedCodes.forEach { code ->
            assertTrue("Expected $code to be supported", languageManager.isSupported(code))
        }
    }

    @Test
    fun `isSupported returns false for unsupported languages`() {
        val unsupportedCodes = listOf("xx", "invalid", "123", "")
        
        unsupportedCodes.forEach { code ->
            assertFalse("Expected $code to be unsupported", languageManager.isSupported(code))
        }
    }

    @Test
    fun `isSupported is case sensitive`() {
        // Assuming language codes are lowercase
        assertFalse(languageManager.isSupported("EN"))
        assertFalse(languageManager.isSupported("Hi"))
        assertTrue(languageManager.isSupported("en"))
    }

    // ==================== getSupportedLanguages Tests ====================

    @Test
    fun `getSupportedLanguages returns non-empty list`() {
        val languages = languageManager.getSupportedLanguages()

        assertTrue(languages.isNotEmpty())
    }

    @Test
    fun `getSupportedLanguages contains English`() {
        val languages = languageManager.getSupportedLanguages()

        assertTrue(languages.any { it.code == "en" })
    }

    @Test
    fun `getSupportedLanguages has unique codes`() {
        val languages = languageManager.getSupportedLanguages()
        val codes = languages.map { it.code }

        assertEquals(codes.size, codes.toSet().size)
    }

    @Test
    fun `getSupportedLanguages has proper language structure`() {
        val languages = languageManager.getSupportedLanguages()

        languages.forEach { lang ->
            assertNotNull(lang.code)
            assertTrue(lang.code.isNotEmpty())
            assertNotNull(lang.name)
            assertTrue(lang.name.isNotEmpty())
            assertNotNull(lang.nativeName)
            assertTrue(lang.nativeName.isNotEmpty())
        }
    }

    // ==================== isRTL Tests ====================

    @Test
    fun `isRTL returns true for Arabic`() {
        assertTrue(languageManager.isRTL("ar"))
    }

    @Test
    fun `isRTL returns true for Hebrew`() {
        assertTrue(languageManager.isRTL("he"))
    }

    @Test
    fun `isRTL returns true for Persian`() {
        assertTrue(languageManager.isRTL("fa"))
    }

    @Test
    fun `isRTL returns true for Urdu`() {
        assertTrue(languageManager.isRTL("ur"))
    }

    @Test
    fun `isRTL returns false for English`() {
        assertFalse(languageManager.isRTL("en"))
    }

    @Test
    fun `isRTL returns false for Hindi`() {
        assertFalse(languageManager.isRTL("hi"))
    }

    @Test
    fun `isRTL returns false for Japanese`() {
        assertFalse(languageManager.isRTL("ja"))
    }

    @Test
    fun `isRTL returns false for Chinese`() {
        assertFalse(languageManager.isRTL("zh"))
    }

    @Test
    fun `isRTL returns false for unknown language`() {
        assertFalse(languageManager.isRTL("xx"))
    }

    // ==================== getLanguageName Tests ====================

    @Test
    fun `getLanguageName returns English for en`() {
        assertEquals("English", languageManager.getLanguageName("en"))
    }

    @Test
    fun `getLanguageName returns Hindi for hi`() {
        assertEquals("Hindi", languageManager.getLanguageName("hi"))
    }

    @Test
    fun `getLanguageName returns code for unknown language`() {
        assertEquals("unknown", languageManager.getLanguageName("unknown"))
    }

    // ==================== getNativeName Tests ====================

    @Test
    fun `getNativeName returns English for en`() {
        assertEquals("English", languageManager.getNativeName("en"))
    }

    @Test
    fun `getNativeName returns Hindi native name`() {
        assertEquals("हिन्दी", languageManager.getNativeName("hi"))
    }

    @Test
    fun `getNativeName returns Japanese native name`() {
        assertEquals("日本語", languageManager.getNativeName("ja"))
    }

    @Test
    fun `getNativeName returns Arabic native name`() {
        assertEquals("العربية", languageManager.getNativeName("ar"))
    }

    @Test
    fun `getNativeName returns code for unknown language`() {
        assertEquals("xyz", languageManager.getNativeName("xyz"))
    }

    // ==================== Edge Cases ====================

    @Test
    fun `empty language code handling`() {
        assertFalse(languageManager.isSupported(""))
        assertFalse(languageManager.isRTL(""))
        assertEquals("", languageManager.getLanguageName(""))
        assertEquals("", languageManager.getNativeName(""))
    }

    @Test
    fun `whitespace language code handling`() {
        assertFalse(languageManager.isSupported(" "))
        assertFalse(languageManager.isRTL(" "))
    }

    @Test
    fun `language codes with special characters`() {
        assertFalse(languageManager.isSupported("en-US"))
        assertFalse(languageManager.isSupported("en_US"))
    }
}
