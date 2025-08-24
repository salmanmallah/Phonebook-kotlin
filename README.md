# 📱 Contact Book - Android App

## 🎯 Project Overview

**Contact Book** is a modern Android application built with **Kotlin** and **Jetpack Compose**. It's designed to be a simple yet elegant contact management system that allows users to add, view, and manage their contacts with a beautiful Material Design 3 interface.

## 🏗️ Project Structure

```
Phonebook-kotlin/
├── 📁 app/                                    # Main application module
│   ├── 📁 build.gradle.kts                   # App-level build configuration
│   ├── 📁 proguard-rules.pro                 # ProGuard rules for code obfuscation
│   └── 📁 src/
│       ├── 📁 androidTest/                   # Android instrumentation tests
│       │   └── 📁 java/com/app/contactbook/
│       │       └── 📄 ExampleInstrumentedTest.kt
│       ├── 📁 main/                          # Main source code
│       │   ├── 📄 AndroidManifest.xml        # App manifest with permissions & activities
│       │   ├── 📁 java/com/app/contactbook/  # Main Kotlin source code
│       │   │   ├── 📄 MainActivity.kt        # Main activity with all UI components
│       │   │   └── 📁 ui/theme/              # UI theme and styling
│       │   │       ├── 📄 Color.kt           # Color definitions
│       │   │       ├── 📄 Theme.kt           # Theme configuration
│       │   │       └── 📄 Type.kt            # Typography definitions
│       │   └── 📁 res/                       # Android resources
│       │       ├── 📁 drawable/              # Drawable resources
│       │       ├── 📁 mipmap/                # App icons
│       │       ├── 📁 values/                # String, color, and theme resources
│       │       └── 📁 xml/                   # XML configuration files
│       └── 📁 test/                          # Unit tests
│           └── 📁 java/com/app/contactbook/
│               └── 📄 ExampleUnitTest.kt
├── 📄 build.gradle.kts                       # Project-level build configuration
├── 📄 settings.gradle.kts                    # Project settings and module inclusion
├── 📁 gradle/                                # Gradle wrapper and version catalog
│   ├── 📄 libs.versions.toml                # Dependency version management
│   └── 📁 wrapper/                           # Gradle wrapper files
├── 📄 gradle.properties                      # Gradle configuration properties
├── 📄 gradlew                                # Gradle wrapper script (Unix)
└── 📄 gradlew.bat                            # Gradle wrapper script (Windows)
```

## 🚀 Technology Stack

### Core Technologies
- **Language**: Kotlin 2.0.21
- **Platform**: Android (API 21+)
- **UI Framework**: Jetpack Compose
- **Build System**: Gradle 8.12.1
- **Architecture**: Single Activity with Navigation Compose

### Key Dependencies
- **AndroidX Core KTX**: 1.17.0
- **Jetpack Compose BOM**: 2024.09.00
- **Navigation Compose**: 2.9.3
- **Material 3**: 1.3.2
- **Lifecycle Runtime**: 2.9.2
- **Activity Compose**: 1.10.1

## 🎨 Features & Functionality

### Current Features
1. **Home Screen**: Welcome interface with navigation buttons
2. **Add Contact Screen**: Form to add new contacts with:
   - Full Name input
   - Phone Number input
   - Email input (optional)
   - Save and Cancel buttons
3. **View Contacts Screen**: Placeholder for displaying saved contacts

### UI Components
- **Material Design 3**: Modern, adaptive design system
- **Responsive Layout**: Adapts to different screen sizes
- **Edge-to-Edge**: Full-screen immersive experience
- **Dynamic Colors**: Android 12+ dynamic color support
- **Dark/Light Theme**: Automatic theme switching

## 🔧 Technical Implementation

### Architecture Pattern
- **Single Activity Architecture**: Uses one MainActivity with multiple screens
- **Navigation Compose**: Declarative navigation between screens
- **Composable Functions**: Modular UI components using Jetpack Compose

### Key Components
1. **MainActivity**: Entry point and navigation host
2. **ContactBookNavHost**: Navigation configuration
3. **HomeScreen**: Main landing page
4. **AddContactScreen**: Contact creation form
5. **ViewContactsScreen**: Contact display (placeholder)

### State Management
- **Local State**: Uses `remember` and `mutableStateOf` for UI state
- **Navigation State**: Managed by Navigation Compose

## 📱 App Configuration

### Build Configuration
- **Minimum SDK**: 21 (Android 5.0 Lollipop)
- **Target SDK**: 36 (Android 14)
- **Compile SDK**: 36
- **Java Version**: 11

### Permissions
- No special permissions required (basic contact management)

## 🚧 Current Status & Future Enhancements

### What's Implemented
✅ Basic app structure and navigation  
✅ Home screen with navigation buttons  
✅ Add contact form with validation-ready fields  
✅ Material Design 3 theming  
✅ Responsive layout design  

### What's Missing (Future Development)
🔲 Contact data persistence (database)  
🔲 Contact list display functionality  
🔲 Contact editing and deletion  
🔲 Search and filtering capabilities  
🔲 Contact import/export features  
🔲 Contact photo support  
🔲 Contact categories/groups  
🔲 Backup and sync functionality  

## 🛠️ Development Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 36
- Kotlin 2.0.21

### Build Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

### Testing
- Unit tests available in `test/` directory
- Instrumentation tests available in `androidTest/` directory

## 📊 Project Metrics

- **Lines of Code**: ~268 (MainActivity.kt)
- **Screens**: 3 (Home, Add Contact, View Contacts)
- **Dependencies**: 15+ AndroidX and Compose libraries
- **Target Devices**: Android 5.0+ (API 21+)

## 🎯 Use Cases

This Contact Book app is ideal for:
- **Personal Use**: Managing personal contacts
- **Learning**: Understanding modern Android development
- **Prototype**: Base for more complex contact management apps
- **Portfolio**: Showcasing Kotlin and Compose skills

## 🔮 Roadmap

### Phase 1 (Current)
- ✅ Basic UI and navigation
- ✅ Contact form structure

### Phase 2 (Next)
- 🔲 Database integration (Room)
- 🔲 Contact list implementation
- 🔲 CRUD operations

### Phase 3 (Future)
- 🔲 Advanced features (search, categories)
- 🔲 Cloud sync
- 🔲 Material You theming

---

**Built with ❤️ using Kotlin and Jetpack Compose**
