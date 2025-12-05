<p align="center">
  <img src="https://img.icons8.com/fluency/128/translation.png" alt="AiLang Logo"/>
</p>

<h1 align="center">🌐 AiLang SDK</h1>

<p align="center">
  <strong>AI-Powered Real-Time Translation Framework for Mobile Apps</strong>
</p>

<p align="center">
  <em>Translate your app into 50+ languages instantly using Google Gemini AI</em>
</p>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android"><img src="https://img.shields.io/maven-central/v/io.github.suraj0834.ailang/ailang-android?style=for-the-badge&label=Maven%20Central&color=blue" alt="Maven Central"/></a>
  <a href="https://www.npmjs.com/package/ailang-react-native"><img src="https://img.shields.io/npm/v/ailang-react-native?style=for-the-badge&label=npm&color=red" alt="npm"/></a>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/React_Native-20232A?style=for-the-badge&logo=react&logoColor=61DAFB" alt="React Native"/>
  <img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript"/>
  <img src="https://img.shields.io/badge/Google%20Gemini-8E75B2?style=for-the-badge&logo=google&logoColor=white" alt="Gemini"/>
</p>

---

## 📦 Published SDKs

| Platform | Package | Version | Install |
|----------|---------|---------|---------|
| **Android** | [`io.github.suraj0834.ailang:ailang-android`](https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android) | ![Maven Central](https://img.shields.io/maven-central/v/io.github.suraj0834.ailang/ailang-android?style=flat-square) | `implementation("io.github.suraj0834.ailang:ailang-android:1.0.1")` |
| **React Native** | [`ailang-react-native`](https://www.npmjs.com/package/ailang-react-native) | ![npm](https://img.shields.io/npm/v/ailang-react-native?style=flat-square) | `npm install ailang-react-native` |

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

## ✨ Features

- 🤖 **AI-Powered** - Uses Google Gemini AI for accurate, context-aware translations
- 🚀 **Real-Time** - Translate strings on-demand without pre-generated files
- 🌍 **50+ Languages** - Support for all major world languages
- 💾 **Smart Caching** - Intelligent caching reduces API calls by 90%+
- 📦 **Lightweight** - Minimal bundle size impact
- 🔒 **Type-Safe** - Full TypeScript/Kotlin support
- ⚡ **Fast** - Cached translations return in <1ms
- 🔄 **Batch Processing** - Efficiently handles multiple translations

---

## 📦 Installation

### Android (Kotlin/Java)

Add to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.suraj0834.ailang:ailang-android:1.0.1")
}
```

Or `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.suraj0834.ailang:ailang-android:1.0.1'
}
```

### React Native

```bash
# Using npm
npm install ailang-react-native

# Using yarn
yarn add ailang-react-native
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
                cacheEnabled = true
            )
        )
    }
}

// 2. Use in Activity/Fragment
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Translate strings
        binding.tvWelcome.text = AiLang.t("Welcome to our app!")
        binding.btnLogin.text = AiLang.t("Login")
        
        // Change language
        AiLang.setLanguage("hi") // Switch to Hindi
    }
}
```

### React Native (TypeScript)

```tsx
// 1. Wrap your app with AiLangProvider
import { AiLangProvider, useAiLang } from 'ailang-react-native';

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
      <Text>{t('welcome', 'Welcome to our app!')}</Text>
      <Button title={t('login', 'Login')} onPress={() => {}} />
      
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

## 🌍 Supported Languages (50+)

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
| `ur` | Urdu | `uk` | Ukrainian | `he` | Hebrew |

And many more...

---

## 📊 Performance

| Metric | Value |
|--------|-------|
| Initial translation (50 strings) | ~1-2s |
| Cached translation | <1ms |
| Memory footprint | ~2-5MB |
| Cache hit rate | 90%+ |

---

## 🔐 Security Best Practices

### ⚠️ Never hardcode your API key!

#### Android
```kotlin
// Use BuildConfig with local.properties
AiLang.init(apiKey = BuildConfig.GEMINI_API_KEY)
```

#### React Native
```typescript
// Use environment variables
import Config from 'react-native-config';
<AiLangProvider apiKey={Config.GEMINI_API_KEY}>
```

---

## 📖 Documentation

- 📚 [Full Documentation](https://suraj0834.github.io/AiLang/)
- 🤖 [Android SDK Docs](https://suraj0834.github.io/AiLang/android.html)
- ⚛️ [React Native SDK Docs](https://suraj0834.github.io/AiLang/react-native.html)

---

## 🔗 Links

| Resource | Link |
|----------|------|
| 📦 Maven Central (Android) | [io.github.suraj0834.ailang:ailang-android](https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android) |
| 📦 npm (React Native) | [ailang-react-native](https://www.npmjs.com/package/ailang-react-native) |
| 📚 Documentation | [suraj0834.github.io/AiLang](https://suraj0834.github.io/AiLang/) |
| 🐛 Issues | [GitHub Issues](https://github.com/Suraj0834/AiLang/issues) |

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

MIT License - see [LICENSE](./LICENSE) for details.

---

<p align="center">
  <b>Made with ❤️ by <a href="https://github.com/Suraj0834">Suraj</a></b>
</p>

<p align="center">
  ⭐ Star this repo if you find it helpful!
</p>
