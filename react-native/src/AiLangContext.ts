import { createContext } from 'react';
import { AiLangContextValue } from './types/context';

/**
 * AiLang Context
 * 
 * Used internally by the provider and hook.
 * You typically won't need to use this directly.
 */
export const AiLangContext = createContext<AiLangContextValue | null>(null);

AiLangContext.displayName = 'AiLangContext';
