import React from 'react';
import { AiLangProvider } from '@ailang/react-native';
import { MainApp } from './MainApp';
import strings from './strings/en.json';

// Get API key from environment variables
const GEMINI_API_KEY = process.env.GEMINI_API_KEY || '';

/**
 * Sample App demonstrating AiLang Provider setup
 */
const App = () => {
  return (
    <AiLangProvider
      apiKey={GEMINI_API_KEY}
      config={{
        defaultLanguage: 'en',
        cacheEnabled: true,
        cacheDuration: 24 * 60 * 60 * 1000, // 24 hours
        maxCacheSize: 10, // 10 MB
        debugMode: __DEV__,
        batchSize: 50,
        timeout: 30000,
        retryCount: 3,
        baseStrings: strings,
      }}
    >
      <MainApp />
    </AiLangProvider>
  );
};

export default App;
