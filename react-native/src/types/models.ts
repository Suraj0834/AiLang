/**
 * AiLang Model Types
 */

/**
 * Language representation
 */
export interface Language {
  /**
   * ISO 639-1 language code (e.g., "en", "hi")
   */
  code: string;

  /**
   * English name of the language
   */
  name: string;

  /**
   * Native name of the language (e.g., "हिन्दी" for Hindi)
   */
  nativeName: string;
}

/**
 * Translation result
 */
export interface TranslationResult {
  /**
   * Original translation key
   */
  key: string;

  /**
   * Original text (base language)
   */
  originalText: string;

  /**
   * Translated text
   */
  translatedText: string;

  /**
   * Target language code
   */
  targetLanguage: string;

  /**
   * Whether this translation was from cache
   */
  fromCache: boolean;
}

/**
 * Translation request
 */
export interface TranslationRequest {
  /**
   * Map of keys to text strings to translate
   */
  strings: Record<string, string>;

  /**
   * Target language code
   */
  targetLanguage: string;
}

/**
 * Cache entry
 */
export interface CacheEntry {
  /**
   * Translated value
   */
  value: string;

  /**
   * Timestamp when cached
   */
  timestamp: number;
}

/**
 * Cache statistics
 */
export interface CacheStats {
  totalEntries: number;
  activeEntries: number;
  expiredEntries: number;
  estimatedSizeKB: number;
}
