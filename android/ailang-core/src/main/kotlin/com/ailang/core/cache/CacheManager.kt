package com.ailang.core.cache

import android.content.Context
import android.content.SharedPreferences
import com.ailang.core.config.AiLangConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Cache Manager - Handles caching of translations
 * 
 * Features:
 * - Memory cache (HashMap) for fast access
 * - Persistent disk cache (SharedPreferences)
 * - Automatic cache expiration
 * - LRU eviction when cache is full
 * 
 * @property context Application context
 * @property config AiLang configuration
 */
internal class CacheManager(
    private val context: Context,
    private val config: AiLangConfig
) {
    
    private val gson = Gson()
    private val memoryCache = mutableMapOf<String, CacheEntry>()
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    companion object {
        private const val PREFS_NAME = "ailang_cache"
        private const val KEY_CACHE_DATA = "cache_data"
        private const val KEY_CACHE_TIMESTAMPS = "cache_timestamps"
    }
    
    init {
        if (config.cacheEnabled) {
            loadFromDisk()
        }
    }
    
    /**
     * Get a cached translation
     * 
     * @param key Translation key
     * @param languageCode Language code
     * @return Cached translation or null if not found/expired
     */
    fun get(key: String, languageCode: String): String? {
        if (!config.cacheEnabled) return null
        
        val cacheKey = createCacheKey(key, languageCode)
        val entry = memoryCache[cacheKey]
        
        return when {
            entry == null -> null
            isExpired(entry) -> {
                remove(key, languageCode)
                null
            }
            else -> entry.value
        }
    }
    
    /**
     * Store a translation in cache
     * 
     * @param key Translation key
     * @param value Translated string
     * @param languageCode Language code
     */
    fun put(key: String, value: String, languageCode: String) {
        if (!config.cacheEnabled) return
        
        val cacheKey = createCacheKey(key, languageCode)
        val entry = CacheEntry(
            value = value,
            timestamp = System.currentTimeMillis()
        )
        
        // Check cache size before adding
        if (shouldEvict()) {
            evictOldest()
        }
        
        memoryCache[cacheKey] = entry
        saveToDisk()
    }
    
    /**
     * Remove a cached translation
     * 
     * @param key Translation key
     * @param languageCode Language code
     */
    fun remove(key: String, languageCode: String) {
        val cacheKey = createCacheKey(key, languageCode)
        memoryCache.remove(cacheKey)
        saveToDisk()
    }
    
    /**
     * Clear all cached translations
     */
    fun clearAll() {
        memoryCache.clear()
        prefs.edit().clear().apply()
    }
    
    /**
     * Clear cache for a specific language
     * 
     * @param languageCode Language code
     */
    fun clearLanguage(languageCode: String) {
        val keysToRemove = memoryCache.keys.filter { 
            it.endsWith("_$languageCode") 
        }
        keysToRemove.forEach { memoryCache.remove(it) }
        saveToDisk()
    }
    
    /**
     * Get cache statistics
     * 
     * @return CacheStats object
     */
    fun getStats(): CacheStats {
        val totalEntries = memoryCache.size
        val expiredEntries = memoryCache.values.count { isExpired(it) }
        val estimatedSize = estimateCacheSize()
        
        return CacheStats(
            totalEntries = totalEntries,
            activeEntries = totalEntries - expiredEntries,
            expiredEntries = expiredEntries,
            estimatedSizeKB = estimatedSize
        )
    }
    
    // ==================== Private Methods ====================
    
    private fun createCacheKey(key: String, languageCode: String): String {
        return "${key}_$languageCode"
    }
    
    private fun isExpired(entry: CacheEntry): Boolean {
        val age = System.currentTimeMillis() - entry.timestamp
        return age > config.cacheDuration
    }
    
    private fun shouldEvict(): Boolean {
        val estimatedSize = estimateCacheSize()
        return estimatedSize > config.maxCacheSize * 1024 // Convert MB to KB
    }
    
    private fun evictOldest() {
        // Remove 20% of oldest entries
        val sortedEntries = memoryCache.entries.sortedBy { it.value.timestamp }
        val evictCount = (memoryCache.size * 0.2).toInt().coerceAtLeast(1)
        
        sortedEntries.take(evictCount).forEach { entry ->
            memoryCache.remove(entry.key)
        }
    }
    
    private fun estimateCacheSize(): Long {
        // Rough estimate: average 100 bytes per entry
        return memoryCache.size * 100L / 1024 // KB
    }
    
    private fun loadFromDisk() {
        try {
            val cacheJson = prefs.getString(KEY_CACHE_DATA, null) ?: return
            val timestampsJson = prefs.getString(KEY_CACHE_TIMESTAMPS, null) ?: return
            
            val cacheData: Map<String, String> = gson.fromJson(
                cacheJson,
                object : TypeToken<Map<String, String>>() {}.type
            )
            val timestamps: Map<String, Long> = gson.fromJson(
                timestampsJson,
                object : TypeToken<Map<String, Long>>() {}.type
            )
            
            cacheData.forEach { (key, value) ->
                val timestamp = timestamps[key] ?: System.currentTimeMillis()
                memoryCache[key] = CacheEntry(value, timestamp)
            }
        } catch (e: Exception) {
            // Clear corrupted cache
            prefs.edit().clear().apply()
        }
    }
    
    private fun saveToDisk() {
        try {
            val cacheData = memoryCache.mapValues { it.value.value }
            val timestamps = memoryCache.mapValues { it.value.timestamp }
            
            prefs.edit()
                .putString(KEY_CACHE_DATA, gson.toJson(cacheData))
                .putString(KEY_CACHE_TIMESTAMPS, gson.toJson(timestamps))
                .apply()
        } catch (e: Exception) {
            // Ignore save errors
        }
    }
    
    /**
     * Cache entry data class
     */
    private data class CacheEntry(
        val value: String,
        val timestamp: Long
    )
    
    /**
     * Cache statistics
     */
    data class CacheStats(
        val totalEntries: Int,
        val activeEntries: Int,
        val expiredEntries: Int,
        val estimatedSizeKB: Long
    )
}
