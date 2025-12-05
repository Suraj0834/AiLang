# AiLang React Native Example

This is a sample React Native app demonstrating the AiLang translation SDK.

## Prerequisites

- Node.js 18+
- React Native CLI
- Xcode (for iOS)
- Android Studio (for Android)

## Setup

1. Install dependencies:
```bash
npm install
```

2. For iOS, install pods:
```bash
cd ios && pod install && cd ..
```

3. Update the API key in `App.tsx`:
```typescript
AiLang.initialize({
  apiKey: 'YOUR_GEMINI_API_KEY', // Replace with your actual API key
  defaultLanguage: 'en',
  enableCache: true,
});
```

## Running

### Android
```bash
npm run android
```

### iOS
```bash
npm run ios
```

## Features Demonstrated

- **Single Translation**: Translate text to a selected target language
- **Batch Translation**: Translate multiple texts at once
- **Caching**: Automatic caching of translations for offline access
- **Language Selection**: Choose from 11 supported languages

## Usage

1. Select a target language from the picker
2. Enter text in the input field
3. Tap "Translate" for single translation
4. Enter multiple lines and tap "Batch" for batch translation
5. Use "Clear Cache" to reset cached translations
