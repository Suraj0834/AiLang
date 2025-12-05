# Contributing to AiLang

Thank you for your interest in contributing to AiLang! This document provides guidelines and instructions for contributing.

## 🌟 Ways to Contribute

1. **Report Bugs** - Found a bug? Open an issue with details
2. **Suggest Features** - Have an idea? We'd love to hear it
3. **Submit PRs** - Fix bugs or add features
4. **Improve Docs** - Help make documentation better
5. **Share** - Star the repo and share with others

## 🛠️ Development Setup

### Prerequisites

- Node.js 18+
- Java 11+ (for Android SDK)
- Android Studio (for Android development)
- Xcode (for iOS development on macOS)

### Android SDK

```bash
# Clone the repo
git clone https://github.com/AiLang/ailang.git
cd ailang/android

# Build the library
./gradlew build

# Run tests
./gradlew test

# Generate documentation
./gradlew dokkaHtml
```

### React Native SDK

```bash
# Navigate to React Native folder
cd ailang/react-native

# Install dependencies
npm install

# Build
npm run build

# Run tests
npm test

# Lint
npm run lint
```

## 📝 Pull Request Process

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/my-feature`
3. **Commit** your changes: `git commit -m 'Add some feature'`
4. **Push** to the branch: `git push origin feature/my-feature`
5. **Open** a Pull Request

### PR Guidelines

- Keep PRs focused on a single change
- Include tests for new functionality
- Update documentation as needed
- Follow existing code style
- Add yourself to CONTRIBUTORS.md (if you want)

## 🧪 Testing

### Android

```kotlin
// Add tests in ailang-core/src/test/kotlin
class AiLangTest {
    @Test
    fun `t() returns key when not initialized`() {
        // Test implementation
    }
}
```

### React Native

```typescript
// Add tests in __tests__ folder
describe('AiLang', () => {
  it('should return translated string', () => {
    // Test implementation
  });
});
```

## 📋 Code Style

### Kotlin

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful names
- Add KDoc comments for public APIs

### TypeScript

- Follow ESLint configuration
- Use TypeScript strict mode
- Document public APIs with JSDoc

## 🐛 Reporting Bugs

When reporting bugs, include:

1. **Description** - What happened?
2. **Steps to Reproduce** - How can we see the bug?
3. **Expected Behavior** - What should happen?
4. **Environment** - OS, SDK version, etc.
5. **Screenshots/Logs** - If applicable

## 💡 Feature Requests

For feature requests, include:

1. **Problem** - What problem does this solve?
2. **Solution** - How do you envision it working?
3. **Alternatives** - What alternatives have you considered?
4. **Additional Context** - Any other information

## 📜 Code of Conduct

Be respectful and inclusive. We're all here to learn and build together.

## 📞 Questions?

- Open a [Discussion](https://github.com/AiLang/ailang/discussions)
- Email: suraj6202k@gmail.com

---

Thank you for contributing! 🙏
