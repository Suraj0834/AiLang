/**
 * AiLang React Native SDK
 * AI-Powered Real-Time Translation Framework
 *
 * @author Suraj
 * @version 1.0.0
 */

export { AiLangProvider } from './AiLangProvider';
export { useAiLang } from './useAiLang';
export { AiLangContext } from './AiLangContext';

// Types
export type { AiLangConfig } from './types/config';
export type { Language, TranslationResult } from './types/models';
export type { AiLangContextValue } from './types/context';

// Services (for advanced usage)
export { TranslationService } from './services/TranslationService';
export { CacheService } from './services/CacheService';
export { LanguageService } from './services/LanguageService';

// Utilities
export { SUPPORTED_LANGUAGES, RTL_LANGUAGES } from './utils/constants';
