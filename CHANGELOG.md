# Changelog

All notable changes to AiLang will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Add support for custom translation providers (OpenAI, Anthropic)
- Add offline mode with downloaded language packs
- Add analytics for translation usage
- Add support for string arrays and pluralization rules
- Performance optimizations

## [1.0.0] - 2025-12-04

### Added
- Initial release of AiLang SDK
- Android (Kotlin) SDK with Maven Central support
- React Native (TypeScript) SDK with npm support
- Core translation functionality using Google Gemini AI
- Support for 50+ languages
- Memory and disk caching with configurable duration
- Batch translation for efficient API usage
- RTL language detection
- Parameterized translations with placeholders
- Simple pluralization support
- Language change listeners
- Test mode for unit testing
- Comprehensive documentation

### Core Features
- `AiLang.t(key)` - Translate a string
- `AiLang.t(key, params)` - Translate with parameters
- `AiLang.setLanguage(code)` - Change language
- `AiLang.getCurrentLanguage()` - Get current language
- `AiLang.getSupportedLanguages()` - List supported languages
- `AiLang.isRTL()` - Check if current language is RTL
- `AiLang.preloadLanguage(code)` - Preload translations
- `AiLang.clearCache()` - Clear translation cache

### Android Specific
- `AiLang.init()` - Initialize the SDK
- `AiLang.addLanguageChangeListener()` - Add listener
- `AiLang.setTestMode()` - Enable test mode

### React Native Specific
- `<AiLangProvider>` - Context provider component
- `useAiLang()` - React hook for translations
- `onLanguageChange()` - Subscribe to language changes

---

## Types of Changes

- **Added** - New features
- **Changed** - Changes in existing functionality
- **Deprecated** - Features that will be removed
- **Removed** - Removed features
- **Fixed** - Bug fixes
- **Security** - Vulnerability fixes
