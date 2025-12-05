<p align="center">
  <img src="https://img.icons8.com/fluency/128/translation.png" alt="AiLang Logo"/>
</p>

<h1 align="center">🌐 AiLang</h1>

<p align="center">
  <strong>AI-Powered Real-Time Translation Framework</strong>
</p>

<p align="center">
  <em>Translate your app into 50+ languages instantly using Google Gemini AI</em>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0.0-blue?style=for-the-badge" alt="Version"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/>
  <img src="https://img.shields.io/badge/Platforms-Android%20%7C%20React%20Native-orange?style=for-the-badge" alt="Platforms"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/React_Native-20232A?style=for-the-badge&logo=react&logoColor=61DAFB" alt="React Native"/>
  <img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript"/>
  <img src="https://img.shields.io/badge/Google%20Gemini-8E75B2?style=for-the-badge&logo=google&logoColor=white" alt="Gemini"/>
</p>

---

## 🚀 What is AiLang?

**AiLang** is a revolutionary AI-powered internationalization (i18n) framework that enables **real-time, on-demand translation** of your application's UI strings. Unlike traditional i18n solutions that require pre-translated string files for each language, AiLang uses Google Gemini AI to translate strings dynamically.

### 🎯 Key Benefits

| Feature | Traditional i18n | AiLang |
|---------|------------------|--------|
| **Setup Time** | Hours/Days per language | Minutes (once) |
| **Languages** | Limited to pre-translated | 50+ instantly |
| **Maintenance** | Update each language file | Zero maintenance |
| **App Size** | Grows with languages | Minimal footprint |
| **New Strings** | Requires translation cycle | Instant support |
| **Context Awareness** | Static translations | AI understands context |

---

## 📦 Installation

### Android (Kotlin/Java)

#### Gradle (build.gradle.kts)
```kotlin
dependencies {
    implementation("io.github.ailang:ailang-android:1.0.0")
}
```

#### Gradle (build.gradle)
```groovy
dependencies {
    implementation 'io.github.ailang:ailang-android:1.0.0'
}
```

### React Native

```bash
# Using npm
npm install @ailang/react-native

# Using yarn
yarn add @ailang/react-native
```

---

## ⚡ Quick Start

### Android (Kotlin)

```kotlin
// 1. Initialize in Application class
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AiLang.init(
            context = this,
            apiKey = BuildConfig.GEMINI_API_KEY,
            config = AiLangConfig(
                defaultLanguage = "en",
                cacheEnabled = true,
                cacheDuration = 24 * 60 * 60 * 1000 // 24 hours
            )
        )
    }
}

// 2. Use in Activity/Fragment
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Translate strings
        binding.tvWelcome.text = AiLang.t("welcome_message")
        binding.btnLogin.text = AiLang.t("login")
        
        // Change language
        AiLang.setLanguage("hi") // Switch to Hindi
        
        // Get current language
        val currentLang = AiLang.getCurrentLanguage()
    }
}
```

### React Native (TypeScript)

```typescript
// 1. Initialize in App.tsx
import { AiLangProvider, useAiLang } from '@ailang/react-native';

const App = () => {
  return (
    <AiLangProvider 
      apiKey={process.env.GEMINI_API_KEY}
      defaultLanguage="en"
      cacheEnabled={true}
    >
      <MainApp />
    </AiLangProvider>
  );
};

// 2. Use in components
const WelcomeScreen = () => {
  const { t, setLanguage, currentLanguage } = useAiLang();
  
  return (
    <View>
      <Text>{t('welcome_message')}</Text>
      <Button title={t('login')} onPress={() => {}} />
      
      {/* Change language */}
      <Button 
        title="Switch to Hindi" 
        onPress={() => setLanguage('hi')} 
      />
    </View>
  );
};
```

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           AILANG ARCHITECTURE                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                         YOUR APPLICATION                             │   │
│   │                                                                      │   │
│   │    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐        │   │
│   │    │   Screen 1   │    │   Screen 2   │    │   Screen N   │        │   │
│   │    └──────┬───────┘    └──────┬───────┘    └──────┬───────┘        │   │
│   │           │                   │                   │                 │   │
│   │           └───────────────────┼───────────────────┘                 │   │
│   │                               │                                      │   │
│   │                               ▼                                      │   │
│   │                    ┌──────────────────────┐                         │   │
│   │                    │    AiLang.t("key")   │  ◄── Simple API         │   │
│   │                    └──────────┬───────────┘                         │   │
│   │                               │                                      │   │
│   └───────────────────────────────┼──────────────────────────────────────┘   │
│                                   │                                          │
│   ┌───────────────────────────────▼──────────────────────────────────────┐   │
│   │                       AILANG CORE ENGINE                              │   │
│   │                                                                       │   │
│   │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐   │   │
│   │  │ TranslationMgr  │  │   CacheManager  │  │  LanguageManager    │   │   │
│   │  │                 │  │                 │  │                     │   │   │
│   │  │ • Batch process │  │ • Memory cache  │  │ • Language codes    │   │   │
│   │  │ • Queue mgmt    │  │ • Disk persist  │  │ • RTL detection     │   │   │
│   │  │ • Rate limiting │  │ • LRU eviction  │  │ • Locale mapping    │   │   │
│   │  └────────┬────────┘  └────────┬────────┘  └──────────┬──────────┘   │   │
│   │           │                    │                      │              │   │
│   │           └────────────────────┼──────────────────────┘              │   │
│   │                                │                                      │   │
│   │                    ┌───────────▼───────────┐                         │   │
│   │                    │  TranslationEngine    │                         │   │
│   │                    │                       │                         │   │
│   │                    │  • API calls          │                         │   │
│   │                    │  • Response parsing   │                         │   │
│   │                    │  • Error handling     │                         │   │
│   │                    │  • Retry logic        │                         │   │
│   │                    └───────────┬───────────┘                         │   │
│   │                                │                                      │   │
│   └────────────────────────────────┼─────────────────────────────────────┘   │
│                                    │                                         │
│                                    ▼                                         │
│   ┌────────────────────────────────────────────────────────────────────┐    │
│   │                      GOOGLE GEMINI AI API                           │    │
│   │                                                                     │    │
│   │   Prompt: "Translate to Hindi: {welcome: 'Welcome', login: '...'}" │    │
│   │   Response: {"welcome": "स्वागत है", "login": "लॉगिन करें"}         │    │
│   │                                                                     │    │
│   └────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🌍 Supported Languages (50+)

<details>
<summary><b>Click to see all supported languages</b></summary>

| Code | Language | Code | Language | Code | Language |
|------|----------|------|----------|------|----------|
| `en` | English | `hi` | Hindi | `es` | Spanish |
| `fr` | French | `de` | German | `it` | Italian |
| `pt` | Portuguese | `ru` | Russian | `ja` | Japanese |
| `ko` | Korean | `zh` | Chinese | `ar` | Arabic |
| `tr` | Turkish | `nl` | Dutch | `pl` | Polish |
| `sv` | Swedish | `th` | Thai | `vi` | Vietnamese |
| `id` | Indonesian | `ms` | Malay | `fil` | Filipino |
| `bn` | Bengali | `ta` | Tamil | `te` | Telugu |
| `mr` | Marathi | `gu` | Gujarati | `kn` | Kannada |
| `ml` | Malayalam | `pa` | Punjabi | `ur` | Urdu |
| `el` | Greek | `cs` | Czech | `ro` | Romanian |
| `hu` | Hungarian | `fi` | Finnish | `no` | Norwegian |
| `da` | Danish | `uk` | Ukrainian | `he` | Hebrew |
| `fa` | Persian | `sw` | Swahili | `af` | Afrikaans |
| `am` | Amharic | `bg` | Bulgarian | `ca` | Catalan |
| `hr` | Croatian | `et` | Estonian | `lv` | Latvian |
| `lt` | Lithuanian | `sk` | Slovak | `sl` | Slovenian |

</details>

---

## 🔧 Configuration

### Android Configuration

```kotlin
AiLang.init(
    context = this,
    apiKey = "YOUR_GEMINI_API_KEY",
    config = AiLangConfig(
        // Default language (fallback)
        defaultLanguage = "en",
        
        // Enable/disable caching
        cacheEnabled = true,
        
        // Cache duration in milliseconds (default: 24 hours)
        cacheDuration = 24 * 60 * 60 * 1000L,
        
        // Maximum cache size in MB (default: 10MB)
        maxCacheSize = 10,
        
        // Enable debug logging
        debugMode = BuildConfig.DEBUG,
        
        // Batch size for translations (default: 50)
        batchSize = 50,
        
        // Request timeout in seconds (default: 30)
        timeout = 30,
        
        // Retry count on failure (default: 3)
        retryCount = 3,
        
        // Custom base strings file path (optional)
        baseStringsPath = "strings_en.json"
    )
)
```

### React Native Configuration

```typescript
<AiLangProvider
  apiKey="YOUR_GEMINI_API_KEY"
  config={{
    defaultLanguage: 'en',
    cacheEnabled: true,
    cacheDuration: 86400000, // 24 hours
    maxCacheSize: 10, // MB
    debugMode: __DEV__,
    batchSize: 50,
    timeout: 30000,
    retryCount: 3,
    baseStrings: require('./strings/en.json'),
  }}
>
  {children}
</AiLangProvider>
```

---

## 📚 API Reference

### Core Methods

| Method | Description | Android | React Native |
|--------|-------------|---------|--------------|
| `t(key)` | Translate a string key | ✅ | ✅ |
| `t(key, params)` | Translate with parameters | ✅ | ✅ |
| `setLanguage(code)` | Change current language | ✅ | ✅ |
| `getCurrentLanguage()` | Get current language code | ✅ | ✅ |
| `getSupportedLanguages()` | List all languages | ✅ | ✅ |
| `preloadLanguage(code)` | Preload translations | ✅ | ✅ |
| `clearCache()` | Clear translation cache | ✅ | ✅ |
| `isRTL()` | Check if current lang is RTL | ✅ | ✅ |

### Advanced Usage

#### Parameterized Translations

```kotlin
// Android
val greeting = AiLang.t("greeting", mapOf("name" to "John"))
// "Hello, {name}!" → "Hello, John!"

// React Native
const greeting = t('greeting', { name: 'John' });
```

#### Pluralization

```kotlin
// Android
val items = AiLang.t("items_count", count = 5)
// "{count} item|{count} items" → "5 items"

// React Native
const items = t('items_count', { count: 5 });
```

#### Language Change Listener

```kotlin
// Android
AiLang.addLanguageChangeListener { newLanguage ->
    // Refresh UI
    recreate()
}

// React Native
const { onLanguageChange } = useAiLang();
useEffect(() => {
  const unsubscribe = onLanguageChange((lang) => {
    console.log('Language changed to:', lang);
  });
  return unsubscribe;
}, []);
```

---

## 🔐 Security Best Practices

### ⚠️ Never hardcode your API key!

#### Android
```kotlin
// ❌ DON'T DO THIS
AiLang.init(apiKey = "AIzaSy...")

// ✅ DO THIS - Use local.properties
// In local.properties:
// GEMINI_API_KEY=your_key_here

// In build.gradle:
def localProperties = new Properties()
localProperties.load(new FileInputStream(rootProject.file("local.properties")))

android {
    defaultConfig {
        buildConfigField "String", "GEMINI_API_KEY", "\"${localProperties['GEMINI_API_KEY']}\""
    }
}

// In code:
AiLang.init(apiKey = BuildConfig.GEMINI_API_KEY)
```

#### React Native
```typescript
// ❌ DON'T DO THIS
<AiLangProvider apiKey="AIzaSy...">

// ✅ DO THIS - Use environment variables
// In .env:
// GEMINI_API_KEY=your_key_here

// In code:
import Config from 'react-native-config';
<AiLangProvider apiKey={Config.GEMINI_API_KEY}>
```

---

## 📊 Performance

| Metric | Value |
|--------|-------|
| Initial translation (50 strings) | ~1-2s |
| Cached translation | <1ms |
| Memory footprint | ~2-5MB |
| Network usage per batch | ~5-10KB |

### Optimization Tips

1. **Enable caching** - Reduces API calls by 90%+
2. **Use batch loading** - Load all strings at app start
3. **Preload languages** - Preload frequently used languages
4. **Use appropriate cache duration** - Balance freshness vs. performance

---

## 🧪 Testing

### Android
```kotlin
// Mock AiLang for unit tests
@Before
fun setup() {
    AiLang.setTestMode(true)
    AiLang.setMockTranslations(mapOf(
        "welcome" to "Welcome",
        "login" to "Login"
    ))
}
```

### React Native
```typescript
// Mock AiLang for tests
jest.mock('@ailang/react-native', () => ({
  useAiLang: () => ({
    t: (key: string) => key,
    setLanguage: jest.fn(),
    currentLanguage: 'en',
  }),
}));
```

---

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.

### Development Setup

```bash
# Clone the repo
git clone https://github.com/AiLang/ailang.git
cd ailang

# Android development
cd android
./gradlew build

# React Native development
cd react-native
npm install
npm run build
```

---

## 📄 License

```
MIT License

Copyright (c) 2025 AiLang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.
```

---

## 🙏 Acknowledgments

- Google Gemini AI for powering translations
- The open-source community

---

<p align="center">
  <b>Made with ❤️ by Suraj</b>
</p>

<p align="center">
  <a href="https://github.com/Suraj0834">GitHub</a> •
  <a href="mailto:suraj6202k@gmail.com">Contact</a>
</p>
