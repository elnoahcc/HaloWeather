<div align="center">
  <img width="250" height="250" alt="halo_weather_logo (1)" src="https://github.com/user-attachments/assets/7fa5d65f-42b5-46b2-aaf0-6d63d1534d39" />

  <div align="center">

</div>
  

  
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
  [![Compose](https://img.shields.io/badge/Compose-1.5.0-green.svg)](https://developer.android.com/jetpack/compose)
  [![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/about/versions/nougat)
  [![License](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)
  
</div>

---


<h1 align="center" style="bold"> Aplikasi cuaca Android modern dengan Jetpack Compose </h1>
HaloWeather adalah aplikasi cuaca Android native yang dirancang dengan pendekatan modern menggunakan Jetpack Compose. Aplikasi ini menyediakan informasi cuaca akurat dan real-time untuk lokasi manapun di dunia dengan antarmuka yang intuitif dan responsif.

Dikembangkan menggunakan arsitektur MVVM dan best practices Android development, HaloWeather menghadirkan pengalaman pengguna yang smooth dengan performa optimal.


<br>


## Fitur Utama
<table align="center">
  <tr>
    <td><img src="https://github.com/user-attachments/assets/52f5f7d9-ffb8-4422-bc98-038f8d1349a0" width="250"></td>
    <td><img src="https://github.com/user-attachments/assets/da6c8833-6ce9-4287-9e2a-92e4fc925af6" width="250"></td>
    <td><img src="https://github.com/user-attachments/assets/e8204172-0fb8-49f3-994b-39d91614d1d0" width="250"></td>
  </tr>
</table>



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
- **Temperatur** - Temperatur yang dirasakan

### Antarmuka Modern
- Implementasi Material 3
- Smooth animations dan transitions
- Adaptive layout untuk berbagai ukuran layar
- Kompatibel UI Dark mode & Light mode

### Pencarian Lokasi
- Cari cuaca berdasarkan nama kota
- Support pencarian global
- Support suggestion pencarian


<br>

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

<br>

## Requirements

- **Android Studio**: Hedgehog (2023.1.1) atau lebih baru
- **Java Development Kit**: JDK 17
- **Gradle**: 8.0+
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36 (Android 15)
- **Compile SDK**: 36

  <br>

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

```android {
    namespace = "com.elnoah.haloweather"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.elnoah.haloweather"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
│   ├── src/main/java/com/elnoah/haloweather/
│   │   ├── data/
│   │   │   ├── api/
│   │   │   ├── model/
│   │   │   └── repository/
│   │   │
│   │   ├── ui/
│   │   │   ├── screens/
│   │   │   ├── components/
│   │   │   └── theme/
│   │   │
│   │   ├── viewmodel/
│   │   │
│   │   ├── navigation/
│   │   │
│   │   └── utils/
│   │
│   ├── res/
│   └── AndroidManifest.xml

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

<br>

## 📄 License

```
MIT License

Copyright (c) 2025 Elnoah

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

**Elnoah Agustinus Markus Manalu**
- GitHub: [@elnoahcc](https://github.com/elnoahcc)
- Email: elnoahamm@gmail.com
- LinkedIn: [elnoahmanalu](https://linkedin.com/in/elnoahmanalu)

## Acknowledgments

- [WeatherAPI.com](https://www.weatherapi.com/) - Weather data provider
- [Material Design](https://m3.material.io/) - Design system & guidelines
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- Community contributors dan testers

---

<div align="center">
  
**Elnoah ©2025**
  

</div>
