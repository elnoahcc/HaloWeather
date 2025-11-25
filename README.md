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


<h2 align="center">Fitur Utama</h2>

<table align="center">
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/738624b2-ed9c-4d1a-9993-2c6c884a29db" width="400">
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/282832a7-c539-4bb0-8cf1-b1101afaa78e" width="400">
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/52f5f7d9-ffb8-4422-bc98-038f8d1349a0" width="220">
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e8204172-0fb8-49f3-994b-39d91614d1d0" width="220">
    </td>
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

<h2 align="center">Tech Stack</h2>

<h3>Core</h3>
<table>
  <tr>
    <td><b>Kotlin</b></td>
    <td>100% Kotlin codebase</td>
  </tr>
  <tr>
    <td><b>Jetpack Compose</b></td>
    <td>Declarative UI framework</td>
  </tr>
  <tr>
    <td><b>Material Design 3</b></td>
    <td>Latest design system from Google</td>
  </tr>
  <tr>
    <td><b>Kotlin Coroutines & Flow</b></td>
    <td>Asynchronous programming</td>
  </tr>
</table>


<h3>Architecture & Libraries</h3>
<table>
  <tr>
    <td><b>MVVM Architecture</b></td>
    <td>Clean separation of concerns</td>
  </tr>
  <tr>
    <td><b>ViewModel</b></td>
    <td>Lifecycle-aware state management</td>
  </tr>
  <tr>
    <td><b>StateFlow</b></td>
    <td>Reactive state holder</td>
  </tr>
  <tr>
    <td><b>Retrofit</b></td>
    <td>Type-safe HTTP client</td>
  </tr>
  <tr>
    <td><b>OkHttp</b></td>
    <td>HTTP & HTTP/2 client</td>
  </tr>
  <tr>
    <td><b>Gson</b></td>
    <td>JSON serialization/deserialization</td>
  </tr>
  <tr>
    <td><b>Coil</b></td>
    <td>Image loading library for Compose</td>
  </tr>
  <tr>
    <td><b>Navigation Compose</b></td>
    <td>In-app navigation</td>
  </tr>
</table>

<br>

### API
- **[WeatherAPI.com](https://www.weatherapi.com/)** - Reliable weather data provider

<br>

<h3>Requirements</h3>
<table>
  <tr>
    <td><b>Android Studio</b></td>
    <td>Hedgehog (2023.1.1) atau lebih baru</td>
  </tr>
  <tr>
    <td><b>Java Development Kit</b></td>
    <td>JDK 17</td>
  </tr>
  <tr>
    <td><b>Gradle</b></td>
    <td>8.0+</td>
  </tr>
  <tr>
    <td><b>Minimum SDK</b></td>
    <td>24 (Android 7.0 Nougat)</td>
  </tr>
  <tr>
    <td><b>Target SDK</b></td>
    <td>36 (Android 15)</td>
  </tr>
  <tr>
    <td><b>Compile SDK</b></td>
    <td>36</td>
  </tr>
</table>


  <br>

<h2 align="center">Memulai Projek</h2>

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

<h2 align="center">Build Commands</h2>

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

<h2 align="center">License</h2>

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

<h2 align="center">Developer</h2>

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
