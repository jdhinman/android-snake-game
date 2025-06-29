# 🚀 Codemagic Setup Guide

## Repository Status: ✅ CONNECTED

Your repository `jdhinman/android-snake-game` is connected to Codemagic.

## 🔧 Next Steps

### ⚠️ Important: Free Tier Limitations

Codemagic API access requires a **team plan**. For free accounts, use the **dashboard only**.

### 1. Use Dashboard for Builds

1. **Login to Codemagic**: https://codemagic.io/apps
2. **Find your app**: Look for `android-snake-game`
3. **Start Build**: Click "Start new build"
4. **Select Branch**: Choose `main`
5. **Build**: Use the updated workflow without keystore

### 3. Trigger Your First Build

```bash
# Start build via API
./scripts/codemagic-api.sh build android-workflow main

# Or via dashboard - recommended for first build
# Visit: https://codemagic.io/apps
```

## 📋 Pre-Build Checklist

### Required Environment Variables (Set in Codemagic)
- [ ] `ANDROID_HOME` (usually auto-configured)
- [ ] `JAVA_HOME` (usually auto-configured)

### Optional Environment Variables
- [ ] `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS` (for Play Store deployment)
- [ ] `GOOGLE_PLAY_TRACK` (internal/alpha/beta/production)

### Code Signing (For Release Builds)
- [ ] Upload keystore file to Codemagic
- [ ] Set keystore password in environment variables
- [ ] Configure signing in `codemagic.yaml`

## 🎯 Expected Build Process

1. **Checkout**: Clone your repository
2. **Dependencies**: Download Gradle dependencies
3. **Build**: Compile and build APK/AAB
4. **Test**: Run unit tests (if configured)
5. **Sign**: Sign release builds with your keystore
6. **Artifacts**: Generate downloadable APK/AAB files
7. **Deploy**: Optionally deploy to Play Store

## 📱 Build Outputs

After successful build, you'll get:
- **Debug APK**: For testing and development
- **Release APK**: For distribution
- **Android App Bundle (AAB)**: For Play Store upload
- **Test Reports**: If tests were run

## 🚀 First Build Recommendation

**Use the Codemagic Dashboard** for your first build to:
1. Verify the workflow configuration
2. Check for any missing dependencies
3. Review build logs for any issues
4. Download and test the generated APK

## 🛠️ Troubleshooting

### Common Issues:
1. **Missing Android SDK**: Usually auto-resolved by Codemagic
2. **Gradle sync issues**: Check `build.gradle` configurations
3. **Signing issues**: Verify keystore upload and configuration
4. **Dependency conflicts**: Review `app/build.gradle` dependencies

### Build Logs:
- Always check build logs for detailed error information
- Look for specific error messages in the failing step
- Common fixes are documented in the repository README

## ✅ Success Indicators

A successful build will show:
- ✅ All build steps completed
- ✅ APK/AAB artifacts generated
- ✅ No compilation errors
- ✅ All tests passed (if running tests)

Your Snake Game is ready for professional CI/CD! 🎮