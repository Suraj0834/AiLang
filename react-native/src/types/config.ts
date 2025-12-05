/**
 * AiLang Configuration Types
 */

export interface AiLangConfig {
  /**
   * Google Gemini API key
   */
  apiKey: string;

  /**
   * Default/fallback language code (e.g., "en")
   * @default "en"
   */
  defaultLanguage?: string;

  /**
   * Enable caching of translations
   * @default true
   */
  cacheEnabled?: boolean;

  /**
   * Cache duration in milliseconds
   * @default 86400000 (24 hours)
   */
  cacheDuration?: number;

  /**
   * Maximum cache size in MB
   * @default 10
   */
  maxCacheSize?: number;

  /**
   * Enable debug logging
   * @default false
   */
  debugMode?: boolean;

  /**
   * Number of strings to translate in a single API call
   * @default 50
   */
  batchSize?: number;

  /**
   * Request timeout in milliseconds
   * @default 30000
   */
  timeout?: number;

  /**
   * Number of retries on failure
   * @default 3
   */
  retryCount?: number;

  /**
   * Base strings object or require path
   * e.g., require('./strings/en.json')
   */
  baseStrings?: Record<string, string>;
}

/**
 * Default configuration values
 */
export const DEFAULT_CONFIG: Required<Omit<AiLangConfig, 'apiKey' | 'baseStrings'>> & {
  baseStrings: Record<string, string>;
} = {
  defaultLanguage: 'en',
  cacheEnabled: true,
  cacheDuration: 24 * 60 * 60 * 1000, // 24 hours
  maxCacheSize: 10,
  debugMode: false,
  batchSize: 50,
  timeout: 30000,
  retryCount: 3,
  baseStrings: {},
};
