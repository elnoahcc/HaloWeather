<div align="center">
  <img src="https://github.com/user-attachments/assets/ddb31571-6d60-4a12-bb9b-6e27df553146" alt="HaloWeather Logo" width="200"/>
  <div align="center">

</div>
  
  **Aplikasi cuaca Android modern dengan Jetpack Compose**
  
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
  [![Compose](https://img.shields.io/badge/Compose-1.5.0-green.svg)](https://developer.android.com/jetpack/compose)
  [![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/about/versions/nougat)
  [![License](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)
  
</div>

---

## Tentang HaloWeather
<div align="center">
  <img width="521" height="499" alt="image" src="https://github.com/user-attachments/assets/15bdbc17-2ba0-4286-9a44-9be373669b7b" />
</div>


HaloWeather adalah aplikasi cuaca Android native yang dirancang dengan pendekatan modern menggunakan Jetpack Compose. Aplikasi ini menyediakan informasi cuaca akurat dan real-time untuk lokasi manapun di dunia dengan antarmuka yang intuitif dan responsif.

Dikembangkan menggunakan arsitektur MVVM dan best practices Android development, HaloWeather menghadirkan pengalaman pengguna yang smooth dengan performa optimal.




## Fitur Utama

### Informasi Cuaca Lengkap
- Suhu real-time dengan deskripsi kondisi cuaca
- Prakiraan cuaca per jam dan harian
- Icon cuaca dinamis yang menyesuaikan kondisi

### Data Detail
- **Kecepatan Angin** - Monitoring kecepatan dan arah angin
- **Kelembapan** - Persentase kelembapan udara
- **Tekanan Udara** - Data tekanan atmosfer
- **Visibility** - Jarak pandang dalam kilometer
- **UV Index** - Indeks radiasi ultraviolet
- **Feels Like** - Temperatur yang dirasakan

### Antarmuka Modern
- Material Design 3 implementation
- Smooth animations dan transitions
- Adaptive layout untuk berbagai ukuran layar
- Dark mode & Light mode dengan theme switching otomatis

### Pencarian Lokasi
- Cari cuaca berdasarkan nama kota
- Support pencarian global
- Simpan lokasi favorit untuk akses cepat

## Tech Stack

### Core
- **Kotlin** - 100% Kotlin codebase
- **Jetpack Compose** - Declarative UI framework
- **Material Design 3** - Latest design system from Google
- **Kotlin Coroutines & Flow** - Asynchronous programming

### Architecture & Libraries
- **MVVM Architecture** - Clean separation of concerns
- **ViewModel** - Lifecycle-aware state management
- **StateFlow** - Reactive state holder
- **Retrofit** - Type-safe HTTP client
- **OkHttp** - HTTP & HTTP/2 client
- **Gson** - JSON serialization/deserialization
- **Coil** - Image loading library for Compose
- **Navigation Compose** - In-app navigation

### API
- **[WeatherAPI.com](https://www.weatherapi.com/)** - Reliable weather data provider

## Requirements

- **Android Studio**: Hedgehog (2023.1.1) atau lebih baru
- **Java Development Kit**: JDK 17
- **Gradle**: 8.0+
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## 🚀 Getting Started

### 1. Clone Repository

```bash
git clone https://github.com/username/haloweather.git
cd haloweather
```

### 2. API Key Setup

Dapatkan API key gratis dari WeatherAPI.com:

1. Kunjungi [weatherapi.com](https://www.weatherapi.com/)
2. Sign up untuk akun gratis
3. Copy API key dari dashboard

Tambahkan API key ke file `local.properties`:

```properties
# local.properties
WEATHER_API_KEY=your_api_key_here
```

> **Note**: File `local.properties` tidak di-commit ke git untuk keamanan. Pastikan API key Anda tetap private.

### 3. Build Configuration

Buka file `build.gradle.kts` dan pastikan konfigurasi sudah sesuai:

```kotlin
android {
    namespace = "com.yourpackage.haloweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yourpackage.haloweather"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
}
```

### 4. Sync & Run

1. Buka project di Android Studio
2. Sync Gradle files
3. Build dan run aplikasi di emulator atau device

## Struktur Project

```
haloweather/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/yourpackage/haloweather/
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/           # API service interfaces
│   │   │   │   │   ├── model/         # Data models & DTOs
│   │   │   │   │   └── repository/    # Repository pattern implementation
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/       # Composable screens
│   │   │   │   │   ├── components/    # Reusable UI components
│   │   │   │   │   └── theme/         # Theme, colors, typography
│   │   │   │   │
│   │   │   │   ├── viewmodel/         # ViewModels untuk state management
│   │   │   │   │
│   │   │   │   ├── navigation/        # Navigation graph
│   │   │   │   │
│   │   │   │   └── utils/             # Helper functions & extensions
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/          # Icons & drawables
│   │   │   │   ├── values/            # Strings, dimensions, colors
│   │   │   │   └── ...
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/                      # Unit tests
│   │       └── androidTest/           # Instrumentation tests
│   │
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## Build Commands

### Development Build
```bash
# Debug APK
./gradlew assembleDebug

# Install ke device
./gradlew installDebug
```

### Production Build
```bash
# Release APK
./gradlew assembleRelease

# Generate App Bundle (AAB)
./gradlew bundleRelease
```

### Testing
```bash
# Unit tests
./gradlew test

# Instrumentation tests
./gradlew connectedAndroidTest
```

## Roadmap

- [ ] Widget home screen
- [ ] Notifikasi cuaca ekstrem
- [ ] Grafik prakiraan interaktif
- [ ] Multi-bahasa support
- [ ] Offline mode dengan caching
- [ ] Location history
- [ ] Share cuaca ke social media
- [ ] Wear OS companion app

## 🤝 Kontribusi

Kontribusi sangat diterima dan diapresiasi! Baik itu bug report, feature request, atau pull request.

### Cara Berkontribusi

1. **Fork** repository ini
2. **Create branch** untuk fitur Anda
   ```bash
   git checkout -b feature/fitur-keren
   ```
3. **Commit** perubahan Anda
   ```bash
   git commit -m 'Menambahkan fitur keren'
   ```
4. **Push** ke branch
   ```bash
   git push origin feature/fitur-keren
   ```
5. **Create Pull Request** dengan deskripsi yang jelas

### Guidelines

- Ikuti coding style yang sudah ada
- Tulis unit test untuk fitur baru
- Update dokumentasi jika diperlukan
- Pastikan semua test passing sebelum submit PR

## 📝 Changelog

### Version 1.0.0 (Initial Release)
- ✨ Tampilan cuaca real-time
- ✨ Detail informasi cuaca lengkap
- ✨ Dark mode & Light mode
- ✨ Pencarian lokasi
- ✨ Material Design 3 UI

## 📄 License

```
MIT License

Copyright (c) 2024 HaloWeather

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Developer

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com
- LinkedIn: [Your Name](https://linkedin.com/in/yourprofile)

## Acknowledgments

- [WeatherAPI.com](https://www.weatherapi.com/) - Weather data provider
- [Material Design](https://m3.material.io/) - Design system & guidelines
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- Community contributors dan testers

---

<div align="center">
  
**Elnoah ©2025**
  

</div>
