# Maven Central Publishing Guide for AiLang

## Prerequisites

1. **Sonatype Account**: Register at [central.sonatype.com](https://central.sonatype.com)
2. **GPG Key**: For signing artifacts
3. **Namespace Verification**: Verify `io.github.ailang` namespace

## Setup Steps

### 1. Generate GPG Key

```bash
# Generate a new GPG key
gpg --full-generate-key

# List keys to get the key ID
gpg --list-secret-keys --keyid-format LONG

# Export your public key to keyserver
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
```

### 2. Configure Gradle Properties

Add to `~/.gradle/gradle.properties`:

```properties
# Sonatype/Maven Central Credentials
mavenCentralUsername=your_sonatype_username
mavenCentralPassword=your_sonatype_password

# GPG Signing
signing.keyId=YOUR_KEY_ID_LAST_8_CHARS
signing.password=YOUR_GPG_PASSWORD
signing.secretKeyRingFile=/Users/YOUR_USER/.gnupg/secring.gpg

# For newer GPG versions, use:
# signing.gnupg.executable=gpg
# signing.gnupg.useLegacyGpg=true
# signing.gnupg.keyName=YOUR_KEY_ID
```

### 3. Publish to Maven Central

```bash
cd android

# Publish to staging (for testing)
./gradlew publishAllPublicationsToMavenCentralRepository

# The library will be available at:
# io.github.ailang:ailang-android:1.0.0
```

### 4. Verify Publication

After publishing, verify at:
- https://central.sonatype.com/artifact/io.github.ailang/ailang-android

## Usage After Publishing

Users can add to their `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.ailang:ailang-android:1.0.0'
}
```

Or in `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.ailang:ailang-android:1.0.0")
}
```

## Versioning

Update version in `android/gradle.properties`:

```properties
VERSION_NAME=1.0.1
```

## Troubleshooting

### GPG Issues on macOS

```bash
# Install GPG
brew install gnupg

# If using GPG 2.1+, export secret key differently
gpg --export-secret-keys YOUR_KEY_ID > ~/.gnupg/secring.gpg
```

### Signing Issues

Make sure your `~/.gradle/gradle.properties` has correct paths and credentials.

## CI/CD Publishing

For automated publishing via GitHub Actions, add secrets:
- `OSSRH_USERNAME`
- `OSSRH_PASSWORD`
- `SIGNING_KEY_ID`
- `SIGNING_PASSWORD`
- `SIGNING_SECRET_KEY_RING_FILE` (base64 encoded)
