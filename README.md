# 🐍 Snake Game Android App

A classic Snake game built for Android using Kotlin with modern Android development practices and Codemagic.io CI/CD integration.

## 🎮 Features

- **Classic Gameplay**: Navigate the snake to eat food and grow longer
- **Touch Controls**: Intuitive swipe gestures for directional control  
- **Smooth Graphics**: 60fps Canvas-based rendering with grid layout
- **Score System**: Real-time scoring with persistent high score storage
- **Game States**: Pause, resume, and game over functionality
- **Modern UI**: Material Design 3 with custom theming

## 🛠️ Technical Stack

- **Language**: Kotlin
- **Target SDK**: Android API 34
- **Min SDK**: Android API 21 (Android 5.0+)
- **Architecture**: MVVM pattern with LiveData
- **Rendering**: Custom View with Canvas
- **Build System**: Gradle 8.4 with Android Gradle Plugin 8.1.2
- **CI/CD**: Codemagic.io integration

## 🏗️ Project Structure

```
snake-game/
├── app/
│   ├── src/main/
│   │   ├── java/com/termux/snake/
│   │   │   ├── MainActivity.kt      # Main activity with UI management
│   │   │   ├── GameView.kt          # Custom view with Canvas rendering
│   │   │   ├── Snake.kt             # Snake entity and movement logic
│   │   │   └── Food.kt              # Food generation and collision
│   │   ├── res/                     # Android resources
│   │   └── AndroidManifest.xml
│   └── build.gradle                 # App-level build configuration
├── codemagic.yaml                   # CI/CD pipeline configuration
├── build.gradle                     # Project-level build configuration
└── settings.gradle                  # Gradle settings
```

## 🚀 Getting Started

### Prerequisites

- Android SDK 34
- Java 17 or higher
- Gradle 8.4+

### Build Instructions

1. **Clone the repository** (or navigate to project directory)
```bash
cd /data/data/com.termux/files/home/projects/snake-game
```

2. **Set up Android SDK path**
```bash
echo "sdk.dir=/data/data/com.termux/files/usr/share/android-sdk" > local.properties
```

3. **Build debug APK**
```bash
./gradlew assembleDebug
```

4. **Build release APK**
```bash
./gradlew assembleRelease
```

### Installation

The built APK will be located at:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## 🔧 Codemagic.io Integration

This project includes comprehensive CI/CD integration with Codemagic.io for automated builds and deployment.

### Workflow Configuration

The `codemagic.yaml` file defines two workflows:

1. **android-workflow**: Main build and release pipeline
   - Builds debug and release APKs
   - Generates Android App Bundle (AAB)
   - Publishes to Google Play Store
   - Sends notifications via email and Slack

2. **android-test-workflow**: Testing pipeline
   - Runs unit tests
   - Executes instrumentation tests
   - Generates test reports

### Codemagic API Integration

#### Starting a Build via API

```bash
curl -X POST \
  https://api.codemagic.io/builds \
  -H 'Content-Type: application/json' \
  -H 'x-auth-token: YOUR_API_TOKEN' \
  -d '{
    "appId": "YOUR_APP_ID",
    "workflowId": "android-workflow",
    "branch": "main"
  }'
```

#### Environment Variables

Configure these variables in Codemagic dashboard:
- `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS`: Google Play service account
- `GOOGLE_PLAY_TRACK`: Play Store track (internal/alpha/beta/production)

### Code Signing

1. **Generate keystore** (if not existing):
```bash
keytool -genkeypair -v -keystore snake-game-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias snake-game
```

2. **Upload keystore** to Codemagic dashboard under Code Signing

## 🎯 Game Controls

- **Swipe Up**: Move snake up
- **Swipe Down**: Move snake down  
- **Swipe Left**: Move snake left
- **Swipe Right**: Move snake right
- **Tap Play Again**: Restart after game over

## 🏆 Game Rules

1. Control the snake to eat red food items
2. Each food item increases score by 10 points
3. Snake grows longer after eating food
4. Game ends if snake hits walls or itself
5. High score is automatically saved

## 🔧 Development

### Local Development Setup

1. **Import into Android Studio**
2. **Sync Gradle** dependencies
3. **Run on device/emulator**
4. **Debug** using Android debugging tools

### Testing

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests (requires connected device)
./gradlew connectedAndroidTest
```

### Performance Optimization

- Game loop runs at consistent 200ms intervals
- Canvas rendering optimized for 60fps
- Efficient collision detection algorithms
- Memory-conscious object pooling

## 📱 Compatibility

- **Minimum Android Version**: 5.0 (API 21)
- **Target Android Version**: 14 (API 34)
- **Screen Orientations**: Portrait (locked)
- **Architecture Support**: ARM64, ARM32, x86, x86_64

## 🔒 Security

- No network permissions required
- Local high score storage only
- No sensitive data collection
- ProGuard obfuscation for release builds

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙋‍♂️ Support

For issues and questions:
- Create an issue on the repository
- Contact: developer@termux.com

---

**Built with ❤️ using Kotlin and Android SDK on Termux**