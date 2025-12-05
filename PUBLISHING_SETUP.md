# 🚀 AiLang SDK Publishing Setup

## GitHub Secrets Required

Go to your GitHub repository → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

Add the following secrets:

### 1. Maven Central (Sonatype) Secrets

| Secret Name | Value |
|-------------|-------|
| `SONATYPE_USERNAME` | Your Sonatype username (the one you used to log in) |
| `SONATYPE_PASSWORD` | Your Sonatype password |

### 2. GPG Signing Secrets

| Secret Name | Value |
|-------------|-------|
| `GPG_KEY_ID` | `D06F68BDBB4E95B2` |
| `GPG_PRIVATE_KEY` | (See base64 encoded key below) |
| `GPG_PASSPHRASE` | (Leave empty - no passphrase was set) |

### 3. npm Publishing Secret

| Secret Name | Value |
|-------------|-------|
| `NPM_TOKEN` | Your npm access token (generate at npmjs.com → Access Tokens) |

---

## GPG Private Key (Base64 Encoded)

Copy this entire string for `GPG_PRIVATE_KEY`:

```
LS0tLS1CRUdJTiBQR1AgUFJJVkFURSBLRVkgQkxPQ0stLS0tLQoKbFFjWUJHa3l6NG9CRUFESktaM0JaSExERjA1NVlYYkNaN09yeXE1anpSb2xTWlR6VStnL3pCWkV5WGhkcElmcgp5Zk4xYWJ0dGp5cUdVRHZyN0t6ZTg5UFRGMmJybUF0V3MrSk5LUWp2c1ZJb1JhaE5JQklOa200NlpZSWIxaEorCnlSWkhFY25PRys4ZENVTW4zMjBVUWY0Rm56VWxYdkRKajdFZzZBc3pNMVVISEM1NVF5UWVtbTZEN2lPdkxCWEkKK2VpUktGbG9oVDROS1F2NFQ4NUFlMWU5SmFJRlJ0a0w3UklaSG5MNUlyRWhuL3pCdGNHbjlORC9GamdCUWNxZwpPb042UVRkZVRFWGlseVZaTCtsR1RONWFyUXQ2aXZ1eFRnb0dUKzladVBwVlZpZnlPbWloYnV1bUhzQUMzcFZ5CkREMXh6N0Y2dTNhL1A1ZERNeEdSenEzZ1BCbWFwRkVObzY3K213SGQ5R3d4Szd0VTRlRjM0QTlHRkdMZnoxYnMKekdpTVFCR1hCSTdKWTdaK1loUWtDdEs0aHpCUllmakN5eUp3MFdRYTBXeUlMSld2cnUwM0U4THB1RlFCdUNnTAplNFB3OHNkSHVLaUc1VGNFVG5JWjFaaHhUTDArQUZPa2hkNUtWdDRIWHROaGZTSEVxdDQ3UzhYZlBncDM5b2x4CndaUEdybDF0azNyakliU1d6bXQ0WVJiK0NZSkFONFpLZjl6WXdtalRVZEdYaDJqcGFYektrcHJtMUkwdmFPclQKZzRkblVWMS9TMDVkbWRnNUxrU1c0WnRZclM4akRZRUxXaVp1S3crbHpac2lvVlFQTUN4S3MzdGVBaXhnTXp4NQp2QUFFTVVvSFhDMng4M05tZ29QQXpRWkx4VEJIdWpNYUcrSDNnRWZkYlZDMmpwM21MM0ZQMnRML2N3QVJBUUFCCkFBLzlHWTVFYTR1OUpVOXMyWnNmVzhPUjZLcThsejJPRmZEd1Q5aTM0aXhRSTBLc2NjcHBEQjcydU5LMXRzMmYKMjVBT05NTGlGQWIwNjZzeG1PVG9MVEVpemFaVXdNMWpBRzV0QmJOT2ZTaytWZ1BocGgzKzhUQWtZNDM5SXh2dwpZakllMzg0UXpQM2ZRc3ZwbEFyK01HcDZyYndIcERtMVo4eFZrOGsxcWFwT2pQWGExRjBYa3NDUTk2bEx4b0t3ClBKQWJqd1c3SmFlbHkxMUxEK2JpeEdoRW9DcWxTa1QxTE5qbTR2TWFIRXpMK2xsOUdaMGRScTE0V09FM3pWa3kKUXY3c1cwQVhkTkM4MU5jMEQwZzExb2pHcnV1YnlaRzlsOHdmWjE5ZFBka0V3MFlYemFXRkVJZnI0Y0srMWphWApvam4yd2xqc0MvMHVTMnBITkhBRFEyWXBObXlUcUd4dFo4aFpTanc5MEh6bUFxK3dCR3RLU2FJUmR4VDVwMHhRCmM4UEVSM1BGNkI2NEVadVpLWnJHVU1rbVF1TkF4Q0ZWTGUzOVRQUi9QK1prWkV6Nmd4YmwwM1BKMFVlSmNWdE0KdmdzTFE4N09GRzYzMHR1QnJCUzBkQmVURFoyVmszT09QMFgrL2JVSWpMb1NYODRiMUZvOThEWlR5TUxhV1dONgppd3JoUzNNaGh4Mk51elc4eDN0MWFFMXN1NE1KZkkzVVhubTc5ejdMeWs4bEpVZXJJc2dBbzl0a0RLT3dNNktFCktzVFQyUU91NG1hNjk2UkdjWmVZMUVTV01qakhWakhvZWhIZUxlejNqYTlUV1hqVTZrakpKME9NTHBsbnExRjIKZkRiM0JjaERiK1J4TXVodXE1cFJMSlRKQmpndHJXYzRmRHlHeDd3UFBKV25PaWtJQU51VU10enZ1eFpJMWc2OApUeUErK0YyU2x5c21Ud29HanlPOGFEN1puRzVQZXI4VkdodHBKYkZEZDhDMVZlU0MyS2tNMVVNYUhCdUlXMmZwClVtTk5kUGZaWk15bmFNYzJGQXdYTUdCZVVaS090d3Ayc1VTUTczOXk2VzVLcGZMZE5qRUxOdnRLTXN2VXQrVmsKeDFJOENVTjV6RkFwUG1IVzRzQU5BY3pYTDlJZnhXci9LV1JUcExwTWFXOHU0L2dQYTdkYzFMWVZBT3l3bTg1egoyV3g2cVFJVzhQOHIyVkZkUTJmbi8yRVM4Ymo3R0hsVTJ4OTYzWkp0U0huTzR2UVI4RVgrYXJGdU9hdHBxOEhrCndVT3dqVkhzRkNIWkc3UDlKZDN2aDdzL2UzNGFWQWxxWko1ZDUxNVlqQjJKVHRRZGprQVhCcTVMNHRnSVNRL0sKOGRUYm1Na0lBT3FIYTJQam41MjJyUUJ2V0tSNVUzZlV1OWZyOEVVZWJTUkRkckV0Q3dHbUhLaVl2cDFxL1BpeQpsNmhaR2NQdTkrVW9XclpodkpXZVkyQW54MjJ6cHhXdEg4bzRnWkgzVVAwQ1N4SUVwWUhZZTV4REdEQWxVZGlNClY4bVFldVF2RVBZTkZHdExKaXNCN0FUc3NwTEMzeGRDUVVBaGZnRjdJT3pKNndwc3Y2SCt4a1oremxJN0g1b2sKaVdOMDRTYjR5WWk3cnhocnZQZ2JpQm1oMjNBY2dnbFN6YlFHU1JhaUNPY3U5RjdGbWxrNDBucUhmR2pSdFpMeQpnQzdDUk1RYzF5MHpGYnZlVWROQUN0bzhNNkQzaXoxQ0lpQ0Y1b29uQTFGSHNJSWNyV0JsZDFPdGNrTkd3ei9ICnowS1c5WWkxVEdzblVKZVNzV1lCSisyOS9oQXVNRnNIL2pOYTZxSit4RFd6Qk1BbGxtbVlvMjl2UjRqUXNobEYKeVZqaDNOdVNpQ3lJUG15YTBPcGU3TFdPUFhZUldKYko0ZUxCemtUV0U0cU1NZmllR0lTNUs5Qm10d2VrU1NWeQpvSDQwa1YxTmdoTHd1djdIK3pRSjRoMjZlV0tDOTFwMjVUT3lnV0NsdVhqU3ZaZnEyRmRyWTVheHIxcEZqR1N6CjF6a0RXQ09VVDZCdVpMb0Q2amxHNnhrbFEvaGYvSXpra1NmeGlEZXRzUllVZURFY2I5bHVsUmViQW8wb2NpdVgKTzEycjZOSVZZSjBGZ2J0S2g0Tmg5QWVFblJLMGgvT3JjclFFWGxkL2F3YVI5ejd2NzJHZFV2UGFROGl5bEpiOQpPSG1jbExyUVRpdlN6MkJ4VENnV2V1dDl5VUJSTjM0c2dBcFNUWVFLS3pySTgraUM0ajJLLytkOEJMUXhVM1Z5CllXb2dRV2xNWVc1bklEeHpkWEpoYWpBNE16UkFkWE5sY25NdWJtOXlaWEJzZVM1bmFYUm9kV0l1WTI5dFBva0MKVndRVEFRZ0FRUlloQkRFamV0OGl5cGJiOVV3YWt0QnZhTDI3VHBXeUJRSnBNcytLQWhzREJRa0R3bWNBQlFzSgpDQWNDQWlJQ0JoVUtDUWdMQWdRV0FnTUJBaDRIQWhlQUFBb0pFTkJ2YUwyN1RwV3lVd0VRQUo2Zjh1czFlNHRBCkFOQ256RDc4c1lLSU9NYW1kRlpZUmI4UERuL3VYNEg0cmpwYXFaNS81dzczWFNSZG1UUE1WNGdkWDJZMVA5bWMKSzBSNktHZEpWSGE2Q2JXWkFOK1JScThwc2J4ZHlNMHg0aTNsUzZvQXBFMTZTMnBhb1FCQkRXdTdJZGRwYjJCYQpsOXFkTkticDNNcElpQ3pyeTRTWjJVU0NscVNWT0ZwYTZCNzhsMUVjRVJLTVhUQk1SdWdjbXUwMmZKMWVXSDZsCnRGOEpoU3BTakduVGpuK0xuSUtvazdzS3Uxa3VJb0tXSm1jN1FnSUJVK0JFcm9nUm5odDhpcHFUSjBROFZzNkgKbzRDRjVuNEpzTDRoaUk0LzBJRERHbzF1Q3pRNVJydHU3dlN2UWVGUHBHekt4VUFMQytScnBhRVBnL2ltWjM4SQo1T1NLSXZ2ZFdIVFBPNlNmUmd4RldPZm8zTGo1dXk0S2hOMUtONUloNHN3ZDVGMFZ3WEhsRTNUTXhXamVhWVA0CnFLUXp5OGU0dkpuaSsvKzZxNWxQQVJ4akR2V0VTWnl1R1Jhc2NPekM1MlppWldoOXJsT2ZxNDNDS05XUEtWRSsKRnl6MGhHamlOQktBYzhMNzdHZENhMEJOVjdHNEg3RnN5MzhhN29zdkRORjhjWmdKbEFJckxqN2xtbE5kNi9pSAp0VDM2SWhwa1QyYkZYVmI0QU43NU1HY051ZHI1YmdWNExuaC9KMFNWbGJXZkF3cHM2aFdkc1ZCbFRTQUZ6cUt3CkFiLzltY2QrbVdoVXBZUDduRW9CK1FETVB5dVdTK2VvTHZNYjhDZ0lJRm5BdTVwL1MyVzZHUkRiYXRPd1lhMWwKZzBWVXJKTFRWa2dyQVg0Vmc3dXU0c3BsdFBmSGdzN3oKPVBmMU4KLS0tLS1FTkQgUEdQIFBSSVZBVEUgS0VZIEJMT0NLLS0tLS0K
```

---

## Step-by-Step Instructions

### Step 1: Add GitHub Secrets

1. Go to: https://github.com/Suraj0834/CineScope_FE/settings/secrets/actions
2. Click "New repository secret" and add each secret listed above

### Step 2: Get npm Token

1. Go to: https://www.npmjs.com/settings/~/tokens
2. Click "Generate New Token" → "Classic Token"
3. Select "Automation" type
4. Copy the token and add it as `NPM_TOKEN` secret

### Step 3: Create a Release

Once secrets are configured:

```bash
# Tag a release
git tag v1.0.0
git push origin v1.0.0
```

This will trigger the release workflow which will:
- ✅ Build and test Android SDK
- ✅ Publish to Maven Central
- ✅ Build and test React Native SDK  
- ✅ Publish to npm

---

## Manual Publishing (Alternative)

### Publish Android SDK Manually

```bash
cd android
./gradlew publishAllPublicationsToMavenCentralRepository \
  -PmavenCentralUsername=YOUR_SONATYPE_USERNAME \
  -PmavenCentralPassword=YOUR_SONATYPE_PASSWORD \
  -Psigning.keyId=D06F68BDBB4E95B2 \
  -Psigning.password= \
  -Psigning.secretKeyRingFile=~/.gnupg/secring.gpg
```

### Publish React Native SDK Manually

```bash
cd react-native
npm login
npm publish --access public
```

---

## Verification

After publishing:

- **Maven Central**: Search for `io.github.ailang` at https://central.sonatype.com/search
- **npm**: Search for `@ailang/react-native` at https://www.npmjs.com/search?q=%40ailang

---

## GPG Key Details

| Property | Value |
|----------|-------|
| Key ID | `D06F68BDBB4E95B2` |
| Full Fingerprint | `31237ADF22CA96DBF54C1A92D06F68BDBB4E95B2` |
| User ID | `Suraj AiLang <suraj0834@users.noreply.github.com>` |
| Expires | December 5, 2027 |
| Published to | keyserver.ubuntu.com |

---

## Troubleshooting

### GPG Key Not Found
If Maven Central can't verify your signature, the key needs time to propagate. Wait 15-30 minutes and try again.

### Namespace Not Verified
Make sure the verification repository `we0erx5lcj` is public on GitHub.

### Build Failures
Check the GitHub Actions logs for detailed error messages.
