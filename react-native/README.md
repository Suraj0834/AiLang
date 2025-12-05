<p align="center">
  <img src="https://img.icons8.com/fluency/96/translation.png" alt="AiLang Logo"/>
</p>

<h1 align="center">ailang-react-native</h1>

<p align="center">
  <strong>🌐 AI-Powered Real-Time Translation for React Native</strong>
</p>

<p align="center">
  <em>Translate your app into 50+ languages instantly using Google Gemini AI</em>
</p>

<p align="center">
  <img src="https://img.shields.io/npm/v/ailang-react-native?style=flat-square&color=blue" alt="npm version"/>
  <img src="https://img.shields.io/npm/dm/ailang-react-native?style=flat-square&color=green" alt="downloads"/>
  <img src="https://img.shields.io/npm/l/ailang-react-native?style=flat-square" alt="license"/>
  <img src="https://img.shields.io/badge/TypeScript-Ready-blue?style=flat-square&logo=typescript" alt="TypeScript"/>
  <img src="https://img.shields.io/badge/React_Native-0.70+-61DAFB?style=flat-square&logo=react" alt="React Native"/>
</p>

---

## ✨ Features

- 🤖 **AI-Powered** - Uses Google Gemini AI for accurate, context-aware translations
- 🚀 **Real-Time** - Translate strings on-demand without pre-generated files
- 🌍 **50+ Languages** - Support for all major world languages
- 💾 **Smart Caching** - Intelligent caching reduces API calls by 90%+
- 📦 **Lightweight** - Minimal bundle size impact (~30KB)
- 🔒 **Type-Safe** - Full TypeScript support with type definitions
- ⚡ **Fast** - Cached translations return in <1ms
- 🔄 **Batch Processing** - Efficiently handles multiple translations

---

## 📦 Installation

```bash
# Using npm
npm install ailang-react-native

# Using yarn
yarn add ailang-react-native
```

### Peer Dependencies

Make sure you have these peer dependencies installed:

```bash
npm install @react-native-async-storage/async-storage
```

---

## 🚀 Quick Start

### 1. Wrap your app with AiLangProvider

```tsx
import React from 'react';
import { AiLangProvider } from 'ailang-react-native';

const App = () => {
  return (
    <AiLangProvider
      apiKey="YOUR_GEMINI_API_KEY"
      defaultLanguage="en"
      cacheEnabled={true}
    >
      <YourApp />
    </AiLangProvider>
  );
};

export default App;
```

### 2. Use the translation hook

```tsx
import React from 'react';
import { View, Text, Button } from 'react-native';
import { useAiLang } from 'ailang-react-native';

const HomeScreen = () => {
  const { t, setLanguage, currentLanguage, isLoading } = useAiLang();

  return (
    <View style={{ padding: 20 }}>
      <Text style={{ fontSize: 24 }}>
        {t('welcome_message', 'Welcome to our app!')}
      </Text>
      
      <Text style={{ marginTop: 10 }}>
        {t('greeting', 'Hello, how are you today?')}
      </Text>

      <Text style={{ marginTop: 20, color: 'gray' }}>
        Current Language: {currentLanguage}
      </Text>

      <View style={{ marginTop: 20, gap: 10 }}>
        <Button title="🇺🇸 English" onPress={() => setLanguage('en')} />
        <Button title="��🇸 Spanish" onPress={() => setLanguage('es')} />
        <Button title="🇫🇷 French" onPress={() => setLanguage('fr')} />
        <Button title="🇮🇳 Hindi" onPress={() => setLanguage('hi')} />
        <Button title="🇯🇵 Japanese" onPress={() => setLanguage('ja')} />
      </View>
    </View>
  );
};

export default HomeScreen;
```

---

## ⚙️ Configuration

### Provider Options

```tsx
<AiLangProvider
  apiKey="YOUR_GEMINI_API_KEY"      // Required: Your Gemini API key
  defaultLanguage="en"              // Default language code
  cacheEnabled={true}               // Enable/disable caching
  cacheDuration={86400000}          // Cache duration in ms (24 hours)
  debugMode={__DEV__}               // Enable debug logging
  baseStrings={{                    // Optional: Base strings for keys
    welcome: 'Welcome',
    login: 'Login',
  }}
>
  {children}
</AiLangProvider>
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `apiKey` | `string` | Required | Your Google Gemini API key |
| `defaultLanguage` | `string` | `'en'` | Default/fallback language code |
| `cacheEnabled` | `boolean` | `true` | Enable translation caching |
| `cacheDuration` | `number` | `86400000` | Cache TTL in milliseconds |
| `debugMode` | `boolean` | `false` | Enable debug logging |
| `baseStrings` | `object` | `{}` | Base strings for translation keys |

---

## 📖 API Reference

### useAiLang Hook

```tsx
const {
  t,                    // Translation function
  setLanguage,          // Change current language
  currentLanguage,      // Current language code
  isLoading,            // Loading state
  isInitialized,        // Initialization state
  supportedLanguages,   // List of supported languages
  clearCache,           // Clear translation cache
  preloadLanguage,      // Preload translations for a language
} = useAiLang();
```

### Translation Function

```tsx
// Basic usage
t('key', 'Default text')

// With parameters
t('greeting', 'Hello, {name}!', { name: 'John' })
// Output: "Hello, John!" or translated equivalent

// Just key (requires baseStrings in config)
t('welcome')
```

### Change Language

```tsx
// Change to Spanish
await setLanguage('es');

// Change to Hindi
await setLanguage('hi');
```

### Preload Language

```tsx
// Preload French translations for faster switching
await preloadLanguage('fr');
```

---

## 🌍 Supported Languages

| Code | Language | Code | Language | Code | Language |
|------|----------|------|----------|------|----------|
| `en` | English | `hi` | Hindi | `es` | Spanish |
| `fr` | French | `de` | German | `it` | Italian |
| `pt` | Portuguese | `ru` | Russian | `ja` | Japanese |
| `ko` | Korean | `zh` | Chinese | `ar` | Arabic |
| `tr` | Turkish | `nl` | Dutch | `pl` | Polish |
| `sv` | Swedish | `th` | Thai | `vi` | Vietnamese |
| `id` | Indonesian | `bn` | Bengali | `ta` | Tamil |
| `te` | Telugu | `mr` | Marathi | `gu` | Gujarati |
| `kn` | Kannada | `ml` | Malayalam | `pa` | Punjabi |
| `ur` | Urdu | ... | and 20+ more! | | |

---

## 🔐 Security

### ⚠️ Never hardcode your API key!

```tsx
// ❌ DON'T DO THIS
<AiLangProvider apiKey="AIzaSy...">

// ✅ DO THIS - Use environment variables
import Config from 'react-native-config';

<AiLangProvider apiKey={Config.GEMINI_API_KEY}>
```

Create a `.env` file:
```
GEMINI_API_KEY=your_api_key_here
```

---

## 📊 Performance

| Metric | Value |
|--------|-------|
| Initial translation (50 strings) | ~1-2s |
| Cached translation | <1ms |
| Bundle size impact | ~30KB |
| Memory footprint | ~2-5MB |

### Tips for Best Performance

1. ✅ **Enable caching** - Reduces API calls by 90%+
2. ✅ **Preload languages** - Use `preloadLanguage()` for common languages
3. ✅ **Use base strings** - Provide fallbacks for offline mode
4. ✅ **Batch translations** - Group related strings together

---

## 🧪 Testing

```tsx
// Mock for Jest tests
jest.mock('ailang-react-native', () => ({
  useAiLang: () => ({
    t: (key: string, fallback: string) => fallback,
    setLanguage: jest.fn(),
    currentLanguage: 'en',
    isLoading: false,
    isInitialized: true,
  }),
  AiLangProvider: ({ children }) => children,
}));
```

---

## 📱 Example App

Check out our [example app](https://github.com/Suraj0834/AiLang/tree/main/react-native/example) for a complete implementation.

---

## 🔗 Links

- 📚 [Full Documentation](https://suraj0834.github.io/AiLang/)
- 🤖 [Android SDK](https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android)
- 🐛 [Report Issues](https://github.com/Suraj0834/AiLang/issues)
- 💻 [GitHub Repository](https://github.com/Suraj0834/AiLang)

---

## 📄 License

MIT License - see [LICENSE](https://github.com/Suraj0834/AiLang/blob/main/LICENSE) for details.

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/Suraj0834">Suraj</a>
</p>

<p align="center">
  ⭐ Star us on GitHub if you find this helpful!
</p>
