import React, { useEffect, useState, useCallback, useRef, ReactNode } from 'react';
import { AiLangContext } from './AiLangContext';
import { AiLangConfig, DEFAULT_CONFIG } from './types/config';
import { AiLangContextValue } from './types/context';
import { Language } from './types/models';
import { TranslationService } from './services/TranslationService';
import { CacheService } from './services/CacheService';
import { LanguageService } from './services/LanguageService';
import { SUPPORTED_LANGUAGES } from './utils/constants';

interface AiLangProviderProps {
  /**
   * Google Gemini API key
   */
  apiKey: string;
  
  /**
   * Configuration options
   */
  config?: Omit<AiLangConfig, 'apiKey'>;
  
  /**
   * Children components
   */
  children: ReactNode;
}

/**
 * AiLang Provider Component
 * 
 * Wrap your app with this provider to enable translations.
 * 
 * @example
 * ```tsx
 * import { AiLangProvider } from '@ailang/react-native';
 * 
 * const App = () => {
 *   return (
 *     <AiLangProvider 
 *       apiKey={process.env.GEMINI_API_KEY}
 *       config={{ defaultLanguage: 'en' }}
 *     >
 *       <MainApp />
 *     </AiLangProvider>
 *   );
 * };
 * ```
 */
export const AiLangProvider: React.FC<AiLangProviderProps> = ({
  apiKey,
  config = {},
  children,
}) => {
  const mergedConfig = { ...DEFAULT_CONFIG, ...config };
  
  // Services
  const translationService = useRef(
    new TranslationService(apiKey, {
      timeout: mergedConfig.timeout,
      retryCount: mergedConfig.retryCount,
      batchSize: mergedConfig.batchSize,
      debugMode: mergedConfig.debugMode,
    })
  );
  
  const cacheService = useRef(
    new CacheService({
      cacheDuration: mergedConfig.cacheDuration,
      maxCacheSize: mergedConfig.maxCacheSize,
      debugMode: mergedConfig.debugMode,
    })
  );
  
  const languageService = useRef(
    new LanguageService(mergedConfig.defaultLanguage)
  );
  
  // State
  const [currentLanguage, setCurrentLanguageState] = useState(
    mergedConfig.defaultLanguage
  );
  const [isLoading, setIsLoading] = useState(false);
  const [translations, setTranslations] = useState<Record<string, string>>({});
  
  // Language change listeners
  const languageListeners = useRef<Set<(language: string) => void>>(new Set());
  
  // Initialize on mount
  useEffect(() => {
    const init = async () => {
      await cacheService.current.init();
      const savedLang = await languageService.current.init();
      
      if (savedLang !== mergedConfig.defaultLanguage) {
        setCurrentLanguageState(savedLang);
        await loadTranslations(savedLang);
      }
    };
    
    init();
  }, []);
  
  /**
   * Load translations for a language
   */
  const loadTranslations = useCallback(
    async (languageCode: string) => {
      if (languageCode === mergedConfig.defaultLanguage) {
        setTranslations({});
        return;
      }
      
      setIsLoading(true);
      
      try {
        // Load from cache first
        const cached = cacheService.current.getAllForLanguage(languageCode);
        if (Object.keys(cached).length > 0) {
          setTranslations(cached);
        }
        
        // Find untranslated strings
        const baseStrings = mergedConfig.baseStrings;
        const untranslated: Record<string, string> = {};
        
        Object.entries(baseStrings).forEach(([key, value]) => {
          if (!cached[key]) {
            untranslated[key] = value;
          }
        });
        
        // Translate missing strings
        if (Object.keys(untranslated).length > 0) {
          const newTranslations = await translationService.current.translateBatch(
            untranslated,
            languageCode
          );
          
          await cacheService.current.putBatch(newTranslations, languageCode);
          
          setTranslations((prev) => ({ ...prev, ...newTranslations }));
        }
      } catch (error) {
        console.error('[AiLang] Failed to load translations:', error);
      } finally {
        setIsLoading(false);
      }
    },
    [mergedConfig.baseStrings, mergedConfig.defaultLanguage]
  );
  
  /**
   * Translate a string key
   */
  const t = useCallback(
    (key: string, params?: Record<string, string | number>): string => {
      let result: string;
      
      if (currentLanguage === mergedConfig.defaultLanguage) {
        result = mergedConfig.baseStrings[key] ?? key;
      } else {
        result = translations[key] ?? mergedConfig.baseStrings[key] ?? key;
      }
      
      // Replace parameters
      if (params) {
        Object.entries(params).forEach(([paramKey, value]) => {
          result = result.replace(`{${paramKey}}`, String(value));
        });
      }
      
      return result;
    },
    [currentLanguage, translations, mergedConfig.baseStrings, mergedConfig.defaultLanguage]
  );
  
  /**
   * Change language
   */
  const setLanguage = useCallback(
    async (languageCode: string) => {
      if (languageCode === currentLanguage) {
        return;
      }
      
      await languageService.current.setCurrentLanguage(languageCode);
      setCurrentLanguageState(languageCode);
      await loadTranslations(languageCode);
      
      // Notify listeners
      languageListeners.current.forEach((listener) => {
        try {
          listener(languageCode);
        } catch (e) {
          console.error('[AiLang] Language listener error:', e);
        }
      });
    },
    [currentLanguage, loadTranslations]
  );
  
  /**
   * Get supported languages
   */
  const getSupportedLanguages = useCallback((): Language[] => {
    return SUPPORTED_LANGUAGES;
  }, []);
  
  /**
   * Check if current language is RTL
   */
  const isRTL = useCallback((): boolean => {
    return languageService.current.isRTL(currentLanguage);
  }, [currentLanguage]);
  
  /**
   * Preload a language
   */
  const preloadLanguage = useCallback(
    async (languageCode: string) => {
      if (languageCode === mergedConfig.defaultLanguage) {
        return;
      }
      
      const untranslated = mergedConfig.baseStrings;
      const translations = await translationService.current.translateBatch(
        untranslated,
        languageCode
      );
      
      await cacheService.current.putBatch(translations, languageCode);
    },
    [mergedConfig.baseStrings, mergedConfig.defaultLanguage]
  );
  
  /**
   * Clear cache
   */
  const clearCache = useCallback(async () => {
    await cacheService.current.clearAll();
    setTranslations({});
  }, []);
  
  /**
   * Add language change listener
   */
  const onLanguageChange = useCallback(
    (callback: (language: string) => void) => {
      languageListeners.current.add(callback);
      return () => {
        languageListeners.current.delete(callback);
      };
    },
    []
  );
  
  const contextValue: AiLangContextValue = {
    t,
    setLanguage,
    currentLanguage,
    isLoading,
    getSupportedLanguages,
    isRTL,
    preloadLanguage,
    clearCache,
    onLanguageChange,
  };
  
  return (
    <AiLangContext.Provider value={contextValue}>
      {children}
    </AiLangContext.Provider>
  );
};
