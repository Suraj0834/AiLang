# npm Publishing Guide for AiLang React Native SDK

## Prerequisites

1. **npm Account**: Create at [npmjs.com](https://www.npmjs.com/signup)
2. **npm CLI**: Already installed with Node.js
3. **Scoped Package**: Using `@ailang/react-native` scope

## Setup Steps

### 1. Login to npm

```bash
npm login
# Enter your username, password, and email
```

### 2. Build the Package

```bash
cd react-native

# Install dependencies
npm install

# Build TypeScript
npm run build

# Run tests
npm run test
```

### 3. Publish to npm

```bash
# First time publishing (scoped package, public)
npm publish --access public

# Subsequent publishes
npm publish
```

### 4. Verify Publication

After publishing, verify at:
- https://www.npmjs.com/package/@ailang/react-native

## Usage After Publishing

Users can install:

```bash
npm install @ailang/react-native
# or
yarn add @ailang/react-native
```

## Versioning

Update version before publishing:

```bash
# Patch release (1.0.0 -> 1.0.1)
npm version patch

# Minor release (1.0.0 -> 1.1.0)
npm version minor

# Major release (1.0.0 -> 2.0.0)
npm version major
```

## Version Tags

```bash
# Publish with a tag (e.g., beta)
npm publish --tag beta

# Install specific tag
npm install @ailang/react-native@beta
```

## Deprecating Versions

```bash
npm deprecate @ailang/react-native@1.0.0 "Critical bug, please upgrade"
```

## Unpublishing (within 72 hours)

```bash
npm unpublish @ailang/react-native@1.0.0
```

## CI/CD Publishing

For automated publishing via GitHub Actions, add npm token:

1. Generate token at npmjs.com/settings/tokens
2. Add as GitHub secret: `NPM_TOKEN`

## release-it Configuration

The package uses `release-it` for automated releases:

```bash
# Interactive release
npm run release

# Non-interactive patch release
npx release-it patch --ci

# Pre-release
npx release-it --preRelease=beta
```

## Troubleshooting

### Scope Not Found

```bash
# Make sure to publish with public access for scoped packages
npm publish --access public
```

### 2FA Required

```bash
# Enable 2FA on npm for publishing
npm profile enable-2fa auth-and-writes
```

### Build Errors

```bash
# Clean and rebuild
rm -rf lib/ node_modules/
npm install
npm run build
```
