<div align="center">

<!-- Hero Section -->
<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6,11,20&height=180&section=header&text=🌐%20AiLang&fontSize=50&fontColor=fff&animation=twinkling&fontAlignY=32&desc=AI-Powered%20Real-Time%20Translation%20SDK&descSize=18&descAlignY=55"/>

<br/>

<!-- Animated typing effect simulation with badges -->
<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=22&pause=1000&color=6366F1&center=true&vCenter=true&multiline=true&repeat=false&width=600&height=80&lines=Translate+your+app+into+50%2B+languages;Powered+by+Google+Gemini+AI" alt="Typing SVG" />

<br/>
<br/>

<!-- Version Badges -->
<a href="https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android">
  <img src="https://img.shields.io/maven-central/v/io.github.suraj0834.ailang/ailang-android?style=for-the-badge&logo=android&logoColor=white&label=Android%20SDK&color=3DDC84" alt="Maven Central"/>
</a>
&nbsp;
<a href="https://www.npmjs.com/package/ailang-react-native">
  <img src="https://img.shields.io/npm/v/ailang-react-native?style=for-the-badge&logo=npm&logoColor=white&label=React%20Native&color=CB3837" alt="npm"/>
</a>
&nbsp;
<img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License"/>

<br/>
<br/>

<!-- Tech Stack Badges -->
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white" alt="TypeScript"/>
<img src="https://img.shields.io/badge/React_Native-61DAFB?style=flat-square&logo=react&logoColor=black" alt="React Native"/>
<img src="https://img.shields.io/badge/Google_Gemini-8E75B2?style=flat-square&logo=google&logoColor=white" alt="Gemini"/>
<img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android"/>

<br/>
<br/>

<!-- Quick Links -->
[📖 Documentation](https://suraj0834.github.io/AiLang/) • 
[🤖 Android SDK](https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android) • 
[⚛️ React Native](https://www.npmjs.com/package/ailang-react-native) • 
[🐛 Report Bug](https://github.com/Suraj0834/AiLang/issues)

</div>

<br/>

---

<br/>

## 🎯 What is AiLang?

<table>
<tr>
<td width="60%">

**AiLang** is a cutting-edge internationalization (i18n) SDK that revolutionizes how mobile apps handle translations. Instead of maintaining separate translation files for each language, AiLang uses **Google Gemini AI** to translate your app's content **in real-time**.

### ✨ Why Choose AiLang?

- 🚀 **Zero Setup Time** - No need to create translation files
- 🌍 **50+ Languages** - Instant support for global audiences  
- 🧠 **Context-Aware** - AI understands context for accurate translations
- 💾 **Smart Caching** - 90%+ cache hit rate, translations in <1ms
- 📦 **Lightweight** - Minimal impact on app size (~30KB)

</td>
<td width="40%">

<div align="center">

```
┌─────────────────────────┐
│     Your App String     │
│   "Welcome to our app"  │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│      🤖 AiLang SDK      │
│   + Google Gemini AI    │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│   🌍 50+ Languages      │
│ "अपने ऐप में आपका स्वागत है" │
│ "Bienvenido a nuestra"  │
│ "アプリへようこそ"          │
└─────────────────────────┘
```

</div>

</td>
</tr>
</table>

<br/>

---

<br/>

## 📦 Published SDKs

<div align="center">

| Platform | Package | Version | Status |
|:--------:|:--------|:-------:|:------:|
| <img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white" width="90"/> | [`io.github.suraj0834.ailang:ailang-android`](https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android) | `1.0.1` | ✅ Live |
| <img src="https://img.shields.io/badge/React_Native-61DAFB?style=flat-square&logo=react&logoColor=black" width="90"/> | [`ailang-react-native`](https://www.npmjs.com/package/ailang-react-native) | `1.0.3` | ✅ Live |

</div>

<br/>

---

<br/>

## 🚀 Quick Start Guide

### Step 1️⃣ Get Your API Key

Before using AiLang, you need a **Google Gemini API Key**:

1. Go to [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Click **"Create API Key"**
3. Copy your API key (keep it secure!)

<br/>

### Step 2️⃣ Install the SDK

<details open>
<summary><b>🤖 Android (Kotlin/Java)</b></summary>

<br/>

Add the dependency to your `build.gradle.kts` (Module level):

```kotlin
dependencies {
    implementation("io.github.suraj0834.ailang:ailang-android:1.0.1")
}
```

<details>
<summary>Using Groovy? (build.gradle)</summary>

```groovy
dependencies {
    implementation 'io.github.suraj0834.ailang:ailang-android:1.0.1'
}
```

</details>

Sync your project with Gradle files.

</details>

<details open>
<summary><b>⚛️ React Native (TypeScript/JavaScript)</b></summary>

<br/>

```bash
# Using npm
npm install ailang-react-native

# Using yarn
yarn add ailang-react-native
```

Install peer dependency:

```bash
npm install @react-native-async-storage/async-storage
```

</details>

<br/>

### Step 3️⃣ Initialize & Use

<details open>
<summary><b>🤖 Android Implementation</b></summary>

<br/>

**1. Store your API key securely in `local.properties`:**

```properties
GEMINI_API_KEY=your_api_key_here
```

**2. Add to your `build.gradle.kts` (app level):**

```kotlin
android {
    defaultConfig {
        buildConfigField("String", "GEMINI_API_KEY", 
            "\"${project.findProperty("GEMINI_API_KEY") ?: ""}\"")
    }
}
```

**3. Initialize in your Application class:**

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize AiLang
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
```

**4. Use in your Activity/Fragment:**

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ✨ Translate any string instantly!
        binding.welcomeText.text = AiLang.t("Welcome to our app!")
        binding.loginButton.text = AiLang.t("Login")
        binding.signupButton.text = AiLang.t("Create Account")
        
        // 🌍 Change language with one line
        binding.hindiButton.setOnClickListener {
            AiLang.setLanguage("hi") // Switch to Hindi
            recreate() // Refresh UI
        }
        
        binding.spanishButton.setOnClickListener {
            AiLang.setLanguage("es") // Switch to Spanish
            recreate()
        }
    }
}
```

</details>

<details open>
<summary><b>⚛️ React Native Implementation</b></summary>

<br/>

**1. Create a `.env` file in your project root:**

```env
GEMINI_API_KEY=your_api_key_here
```

**2. Wrap your app with AiLangProvider:**

```tsx
// App.tsx
import React from 'react';
import { AiLangProvider } from 'ailang-react-native';
import Config from 'react-native-config';
import MainScreen from './MainScreen';

const App = () => {
  return (
    <AiLangProvider
      apiKey={Config.GEMINI_API_KEY}
      defaultLanguage="en"
      cacheEnabled={true}
      cacheDuration={86400000} // 24 hours in ms
    >
      <MainScreen />
    </AiLangProvider>
  );
};

export default App;
```

**3. Use the translation hook in any component:**

```tsx
// MainScreen.tsx
import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { useAiLang } from 'ailang-react-native';

const MainScreen = () => {
  const { t, setLanguage, currentLanguage, isLoading } = useAiLang();

  return (
    <View style={styles.container}>
      {/* ✨ Translate any text */}
      <Text style={styles.welcome}>
        {t('welcome', 'Welcome to our app!')}
      </Text>
      
      <Text style={styles.subtitle}>
        {t('subtitle', 'Translate anything instantly')}
      </Text>

      {/* 🌍 Current language indicator */}
      <Text style={styles.language}>
        Current: {currentLanguage.toUpperCase()}
      </Text>

      {/* 🔄 Language switcher buttons */}
      <View style={styles.buttons}>
        <TouchableOpacity 
          style={styles.button}
          onPress={() => setLanguage('en')}
        >
          <Text>🇺🇸 English</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={styles.button}
          onPress={() => setLanguage('hi')}
        >
          <Text>🇮🇳 Hindi</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={styles.button}
          onPress={() => setLanguage('es')}
        >
          <Text>🇪🇸 Spanish</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={styles.button}
          onPress={() => setLanguage('ja')}
        >
          <Text>🇯🇵 Japanese</Text>
        </TouchableOpacity>
      </View>
      
      {isLoading && <Text>Translating...</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 20 },
  welcome: { fontSize: 28, fontWeight: 'bold', marginBottom: 10 },
  subtitle: { fontSize: 16, color: '#666', marginBottom: 20 },
  language: { fontSize: 14, color: '#888', marginBottom: 30 },
  buttons: { flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'center', gap: 10 },
  button: { padding: 12, backgroundColor: '#f0f0f0', borderRadius: 8, minWidth: 100, alignItems: 'center' }
});

export default MainScreen;
```

</details>

<br/>

---

<br/>

## 🌍 Supported Languages

<div align="center">

AiLang supports **50+ languages** out of the box. Here are some popular ones:

| | | | | |
|:--:|:--:|:--:|:--:|:--:|
| 🇺🇸 English `en` | 🇮🇳 Hindi `hi` | 🇪🇸 Spanish `es` | 🇫🇷 French `fr` | 🇩🇪 German `de` |
| ��🇹 Italian `it` | 🇵🇹 Portuguese `pt` | 🇷🇺 Russian `ru` | 🇯🇵 Japanese `ja` | 🇰🇷 Korean `ko` |
| 🇨🇳 Chinese `zh` | 🇸🇦 Arabic `ar` | 🇹🇷 Turkish `tr` | 🇳🇱 Dutch `nl` | 🇵🇱 Polish `pl` |
| 🇸🇪 Swedish `sv` | 🇹🇭 Thai `th` | 🇻🇳 Vietnamese `vi` | 🇮🇩 Indonesian `id` | 🇧🇩 Bengali `bn` |
| 🇮🇳 Tamil `ta` | 🇮🇳 Telugu `te` | 🇮🇳 Marathi `mr` | 🇮🇳 Gujarati `gu` | 🇮🇳 Kannada `kn` |
| 🇮🇳 Malayalam `ml` | 🇮🇳 Punjabi `pa` | ��🇰 Urdu `ur` | 🇺🇦 Ukrainian `uk` | 🇮🇱 Hebrew `he` |

<br/>

**And many more!** Just pass any language code to `setLanguage()`.

</div>

<br/>

---

<br/>

## 📊 Performance

<div align="center">

| Metric | Value | Notes |
|:------:|:-----:|:------|
| 🚀 **First Translation** | ~1-2s | Initial API call to Gemini |
| ⚡ **Cached Translation** | <1ms | Instant from local cache |
| 💾 **Cache Hit Rate** | 90%+ | Most strings are cached |
| 📦 **SDK Size** | ~30KB | Minimal impact on app |
| 🧠 **Memory Usage** | ~2-5MB | Efficient caching |

</div>

<br/>

---

<br/>

## 🔐 Security Best Practices

> ⚠️ **Important:** Never hardcode your API key in source code!

<table>
<tr>
<td>

### ❌ Don't Do This

```kotlin
// DANGEROUS - Never do this!
AiLang.init(apiKey = "AIzaSy...")
```

</td>
<td>

### ✅ Do This Instead

```kotlin
// Safe - Use BuildConfig
AiLang.init(apiKey = BuildConfig.GEMINI_API_KEY)
```

</td>
</tr>
</table>

**Recommended approach:**
1. Store API key in `local.properties` (Android) or `.env` (React Native)
2. Add these files to `.gitignore`
3. Use environment variables in CI/CD

<br/>

---

<br/>

## 📖 API Reference

<details>
<summary><b>🤖 Android API</b></summary>

<br/>

```kotlin
// Initialize SDK
AiLang.init(context, apiKey, config)

// Translate a string
val translated = AiLang.t("Hello World")

// Translate with key for caching
val translated = AiLang.t("greeting", "Hello World")

// Change language
AiLang.setLanguage("es")

// Get current language
val lang = AiLang.currentLanguage

// Clear cache
AiLang.clearCache()

// Check if initialized
val ready = AiLang.isInitialized
```

</details>

<details>
<summary><b>⚛️ React Native API</b></summary>

<br/>

```typescript
const {
  t,                    // Translation function
  setLanguage,          // Change language
  currentLanguage,      // Current language code
  isLoading,            // Loading state
  isInitialized,        // SDK ready state
  supportedLanguages,   // List of languages
  clearCache,           // Clear translation cache
  preloadLanguage,      // Preload a language
} = useAiLang();

// Translation function
t('key', 'Default text')
t('greeting', 'Hello, {name}!', { name: 'John' })

// Change language
await setLanguage('fr');

// Preload for faster switching
await preloadLanguage('de');
```

</details>

<br/>

---

<br/>

## 🆚 Traditional i18n vs AiLang

<div align="center">

| Aspect | Traditional i18n | AiLang |
|:-------|:----------------:|:------:|
| **Setup Time** | Hours/Days per language | ✅ Minutes (one-time) |
| **New Language** | Create translation file | ✅ Just use it |
| **New String** | Update all language files | ✅ Auto-translated |
| **Maintenance** | High (sync all files) | ✅ Zero |
| **Context Awareness** | ❌ Static text | ✅ AI understands context |
| **App Size** | Grows with languages | ✅ Constant (~30KB) |
| **Offline Support** | ✅ Full | ⚡ Cached translations |
| **Translation Quality** | Depends on translator | ✅ AI + Context |

</div>

<br/>

---

<br/>

## 🛠️ Project Structure

```
AiLang/
├── android/                    # Android SDK source
│   ├── src/main/kotlin/       # Kotlin implementation
│   └── build.gradle.kts       # Gradle config
├── react-native/              # React Native SDK source
│   ├── src/                   # TypeScript implementation
│   └── package.json           # npm config
├── docs/                      # Documentation website
├── examples/                  # Sample applications
└── README.md                  # You are here!
```

<br/>

---

<br/>

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. 🍴 Fork the repository
2. 🌿 Create a feature branch (`git checkout -b feature/amazing-feature`)
3. 💾 Commit your changes (`git commit -m 'Add amazing feature'`)
4. 📤 Push to the branch (`git push origin feature/amazing-feature`)
5. 🔃 Open a Pull Request

<br/>

---

<br/>

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](./LICENSE) file for details.

<br/>

---

<br/>

<div align="center">

## ⭐ Support the Project

If you find AiLang helpful, please consider giving it a star!

[![Star History Chart](https://api.star-history.com/svg?repos=Suraj0834/AiLang&type=Date)](https://star-history.com/#Suraj0834/AiLang&Date)

<br/>

### 🔗 Quick Links

[📦 Maven Central](https://central.sonatype.com/artifact/io.github.suraj0834.ailang/ailang-android) • 
[📦 npm Registry](https://www.npmjs.com/package/ailang-react-native) • 
[📖 Documentation](https://suraj0834.github.io/AiLang/) • 
[🐛 Report Issues](https://github.com/Suraj0834/AiLang/issues)

<br/>

---

<br/>

<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6,11,20&height=100&section=footer"/>

**Made with ❤️ by [Suraj](https://github.com/Suraj0834)**

<br/>

<a href="https://github.com/Suraj0834">
  <img src="https://img.shields.io/badge/GitHub-Suraj0834-181717?style=for-the-badge&logo=github" alt="GitHub"/>
</a>

</div>
