import AsyncStorage from '@react-native-async-storage/async-storage';
import { CacheEntry, CacheStats } from '../types/models';
import { CACHE_KEYS, DEFAULTS } from '../utils/constants';

/**
 * Cache Service - Handles caching of translations using AsyncStorage
 */
export class CacheService {
  private memoryCache: Map<string, CacheEntry> = new Map();
  private cacheDuration: number;
  private maxCacheSize: number;
  private debugMode: boolean;

  constructor(options?: {
    cacheDuration?: number;
    maxCacheSize?: number;
    debugMode?: boolean;
  }) {
    this.cacheDuration = options?.cacheDuration ?? DEFAULTS.CACHE_DURATION;
    this.maxCacheSize = options?.maxCacheSize ?? 10;
    this.debugMode = options?.debugMode ?? false;
  }

  /**
   * Initialize cache by loading from AsyncStorage
   */
  async init(): Promise<void> {
    try {
      const cacheJson = await AsyncStorage.getItem(CACHE_KEYS.TRANSLATIONS);
      if (cacheJson) {
        const cacheData = JSON.parse(cacheJson) as Record<string, CacheEntry>;
        Object.entries(cacheData).forEach(([key, entry]) => {
          this.memoryCache.set(key, entry);
        });
        this.log(`Loaded ${this.memoryCache.size} cached translations`);
      }
    } catch (error) {
      this.log(`Failed to load cache: ${(error as Error).message}`);
    }
  }

  /**
   * Get a cached translation
   */
  get(key: string, languageCode: string): string | null {
    const cacheKey = this.createCacheKey(key, languageCode);
    const entry = this.memoryCache.get(cacheKey);

    if (!entry) {
      return null;
    }

    if (this.isExpired(entry)) {
      this.memoryCache.delete(cacheKey);
      return null;
    }

    return entry.value;
  }

  /**
   * Store a translation in cache
   */
  async put(key: string, value: string, languageCode: string): Promise<void> {
    const cacheKey = this.createCacheKey(key, languageCode);
    const entry: CacheEntry = {
      value,
      timestamp: Date.now(),
    };

    // Check cache size before adding
    if (this.shouldEvict()) {
      this.evictOldest();
    }

    this.memoryCache.set(cacheKey, entry);
    await this.saveToDisk();
  }

  /**
   * Store multiple translations
   */
  async putBatch(
    translations: Record<string, string>,
    languageCode: string
  ): Promise<void> {
    const timestamp = Date.now();

    Object.entries(translations).forEach(([key, value]) => {
      const cacheKey = this.createCacheKey(key, languageCode);
      this.memoryCache.set(cacheKey, { value, timestamp });
    });

    await this.saveToDisk();
  }

  /**
   * Remove a cached translation
   */
  async remove(key: string, languageCode: string): Promise<void> {
    const cacheKey = this.createCacheKey(key, languageCode);
    this.memoryCache.delete(cacheKey);
    await this.saveToDisk();
  }

  /**
   * Clear all cached translations
   */
  async clearAll(): Promise<void> {
    this.memoryCache.clear();
    await AsyncStorage.removeItem(CACHE_KEYS.TRANSLATIONS);
    this.log('Cache cleared');
  }

  /**
   * Clear cache for a specific language
   */
  async clearLanguage(languageCode: string): Promise<void> {
    const keysToDelete: string[] = [];
    this.memoryCache.forEach((_, key) => {
      if (key.endsWith(`_${languageCode}`)) {
        keysToDelete.push(key);
      }
    });

    keysToDelete.forEach((key) => this.memoryCache.delete(key));
    await this.saveToDisk();
    this.log(`Cleared cache for language: ${languageCode}`);
  }

  /**
   * Get cache statistics
   */
  getStats(): CacheStats {
    let expiredCount = 0;
    this.memoryCache.forEach((entry) => {
      if (this.isExpired(entry)) {
        expiredCount++;
      }
    });

    return {
      totalEntries: this.memoryCache.size,
      activeEntries: this.memoryCache.size - expiredCount,
      expiredEntries: expiredCount,
      estimatedSizeKB: this.estimateCacheSize(),
    };
  }

  /**
   * Get all cached translations for a language
   */
  getAllForLanguage(languageCode: string): Record<string, string> {
    const result: Record<string, string> = {};

    this.memoryCache.forEach((entry, cacheKey) => {
      if (cacheKey.endsWith(`_${languageCode}`) && !this.isExpired(entry)) {
        const key = cacheKey.replace(`_${languageCode}`, '');
        result[key] = entry.value;
      }
    });

    return result;
  }

  // ==================== Private Methods ====================

  private createCacheKey(key: string, languageCode: string): string {
    return `${key}_${languageCode}`;
  }

  private isExpired(entry: CacheEntry): boolean {
    const age = Date.now() - entry.timestamp;
    return age > this.cacheDuration;
  }

  private shouldEvict(): boolean {
    const estimatedSize = this.estimateCacheSize();
    return estimatedSize > this.maxCacheSize * 1024; // Convert MB to KB
  }

  private evictOldest(): void {
    // Sort by timestamp and remove oldest 20%
    const entries = Array.from(this.memoryCache.entries()).sort(
      (a, b) => a[1].timestamp - b[1].timestamp
    );

    const evictCount = Math.max(1, Math.floor(entries.length * 0.2));
    entries.slice(0, evictCount).forEach(([key]) => {
      this.memoryCache.delete(key);
    });
  }

  private estimateCacheSize(): number {
    // Rough estimate: ~100 bytes per entry
    return (this.memoryCache.size * 100) / 1024; // KB
  }

  private async saveToDisk(): Promise<void> {
    try {
      const cacheObj: Record<string, CacheEntry> = {};
      this.memoryCache.forEach((entry, key) => {
        cacheObj[key] = entry;
      });

      await AsyncStorage.setItem(CACHE_KEYS.TRANSLATIONS, JSON.stringify(cacheObj));
    } catch (error) {
      this.log(`Failed to save cache: ${(error as Error).message}`);
    }
  }

  private log(message: string): void {
    if (this.debugMode) {
      console.log(`[AiLang Cache] ${message}`);
    }
  }
}
