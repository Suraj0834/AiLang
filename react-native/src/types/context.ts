import { Language } from './models';

/**
 * AiLang Context Value Type
 */
export interface AiLangContextValue {
  /**
   * Translate a string key
   * @param key Translation key
   * @param params Optional parameters for interpolation
   * @returns Translated string
   */
  t: (key: string, params?: Record<string, string | number>) => string;

  /**
   * Set the current language
   * @param languageCode ISO 639-1 language code
   */
  setLanguage: (languageCode: string) => void;

  /**
   * Current language code
   */
  currentLanguage: string;

  /**
   * Whether translations are loading
   */
  isLoading: boolean;

  /**
   * Get all supported languages
   */
  getSupportedLanguages: () => Language[];

  /**
   * Check if current language is RTL
   */
  isRTL: () => boolean;

  /**
   * Preload translations for a language
   * @param languageCode Language code to preload
   */
  preloadLanguage: (languageCode: string) => Promise<void>;

  /**
   * Clear translation cache
   */
  clearCache: () => Promise<void>;

  /**
   * Add a language change listener
   * @param callback Function to call when language changes
   * @returns Unsubscribe function
   */
  onLanguageChange: (callback: (language: string) => void) => () => void;
}
