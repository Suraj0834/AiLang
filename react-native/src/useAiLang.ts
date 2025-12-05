import { useContext } from 'react';
import { AiLangContext } from './AiLangContext';
import { AiLangContextValue } from './types/context';

/**
 * useAiLang Hook
 * 
 * Access translation functionality in your components.
 * 
 * @example
 * ```tsx
 * import { useAiLang } from '@ailang/react-native';
 * 
 * const MyComponent = () => {
 *   const { t, setLanguage, currentLanguage } = useAiLang();
 *   
 *   return (
 *     <View>
 *       <Text>{t('hello_world')}</Text>
 *       <Button 
 *         title="Switch to Hindi" 
 *         onPress={() => setLanguage('hi')} 
 *       />
 *     </View>
 *   );
 * };
 * ```
 */
export function useAiLang(): AiLangContextValue {
  const context = useContext(AiLangContext);
  
  if (!context) {
    throw new Error(
      'useAiLang must be used within an AiLangProvider. ' +
      'Wrap your app with <AiLangProvider> to use this hook.'
    );
  }
  
  return context;
}
