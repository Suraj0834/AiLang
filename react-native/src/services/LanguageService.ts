import AsyncStorage from '@react-native-async-storage/async-storage';
import { Language } from '../types/models';
import { SUPPORTED_LANGUAGES, RTL_LANGUAGES, CACHE_KEYS, DEFAULTS } from '../utils/constants';

/**
 * Language Service - Handles language selection and detection
 */
export class LanguageService {
  private currentLanguage: string;
  private defaultLanguage: string;

  constructor(defaultLanguage: string = DEFAULTS.LANGUAGE) {
    this.defaultLanguage = defaultLanguage;
    this.currentLanguage = defaultLanguage;
  }

  /**
   * Initialize by loading saved language preference
   */
  async init(): Promise<string> {
    try {
      const savedLanguage = await AsyncStorage.getItem(CACHE_KEYS.CURRENT_LANGUAGE);
      if (savedLanguage && this.isSupported(savedLanguage)) {
        this.currentLanguage = savedLanguage;
      }
    } catch (error) {
      console.log(`[AiLang] Failed to load language preference: ${(error as Error).message}`);
    }
    return this.currentLanguage;
  }

  /**
   * Get current language code
   */
  getCurrentLanguage(): string {
    return this.currentLanguage;
  }

  /**
   * Set current language
   */
  async setCurrentLanguage(languageCode: string): Promise<void> {
    if (!this.isSupported(languageCode)) {
      console.warn(`[AiLang] Language ${languageCode} is not supported`);
      return;
    }

    this.currentLanguage = languageCode;
    await AsyncStorage.setItem(CACHE_KEYS.CURRENT_LANGUAGE, languageCode);
  }

  /**
   * Check if a language is supported
   */
  isSupported(languageCode: string): boolean {
    return SUPPORTED_LANGUAGES.some((lang) => lang.code === languageCode);
  }

  /**
   * Get all supported languages
   */
  getSupportedLanguages(): Language[] {
    return [...SUPPORTED_LANGUAGES];
  }

  /**
   * Check if a language is RTL
   */
  isRTL(languageCode?: string): boolean {
    const code = languageCode ?? this.currentLanguage;
    return RTL_LANGUAGES.has(code);
  }

  /**
   * Get language display name
   */
  getLanguageName(languageCode: string): string {
    const lang = SUPPORTED_LANGUAGES.find((l) => l.code === languageCode);
    return lang?.name ?? languageCode;
  }

  /**
   * Get native language name
   */
  getNativeName(languageCode: string): string {
    const lang = SUPPORTED_LANGUAGES.find((l) => l.code === languageCode);
    return lang?.nativeName ?? languageCode;
  }

  /**
   * Get default language
   */
  getDefaultLanguage(): string {
    return this.defaultLanguage;
  }
}
