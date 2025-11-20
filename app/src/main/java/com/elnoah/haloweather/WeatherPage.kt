package com.elnoah.haloweather

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.elnoah.haloweather.api.LocationSuggestion
import com.elnoah.haloweather.api.NetworkResponse
import com.elnoah.haloweather.api.WeatherModel
import com.elnoah.haloweather.ui.theme.StackSansText
import com.elnoah.haloweather.ui.theme.Typography
import kotlin.random.Random

private val BlueLightScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D47A1),
    background = Color(0xFFF5F9FF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E)
)

private val BlueDarkScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = Color(0xFFD0E8FF),
    background = Color(0xFF0A1929),
    onBackground = Color(0xFFE3F2FD),
    surface = Color(0xFF0D1B2A),
    onSurface = Color(0xFFE3F2FD)
)

@Composable
fun HaloBlueTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) BlueDarkScheme else BlueLightScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    HaloBlueTheme {
        var showCurrentLocation by remember { mutableStateOf(true) }

        // Reset ke current location saat back dari search
        BackHandler(enabled = !showCurrentLocation) {
            showCurrentLocation = true
            viewModel.getData("Surakarta") // Reload lokasi saat ini
        }

        if (showCurrentLocation) {
            CurrentLocationWeatherCard(
                viewModel = viewModel,
                onNavigateToSearch = { showCurrentLocation = false }
            )
        } else {
            WeatherSearchPage(
                viewModel = viewModel,
                onBackToHome = { showCurrentLocation = true }
            )
        }
    }
}

@Composable
fun CurrentLocationWeatherCard(
    viewModel: WeatherViewModel,
    onNavigateToSearch: () -> Unit
) {
    val weatherResult = viewModel.weatherResult.observeAsState()
    val colors = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    // Animasi untuk card
    val infiniteTransition = rememberInfiniteTransition(label = "card_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    LaunchedEffect(Unit) {
        // Auto-detect lokasi saat ini (gunakan city default atau GPS)
        viewModel.getData("Surakarta") // Ganti dengan lokasi user atau GPS
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            Color(0xFF0A1929),
                            Color(0xFF1565C0),
                            Color(0xFF1E88E5)
                        )
                    } else {
                        listOf(
                            Color(0xFF42A5F5),
                            Color(0xFF1E88E5),
                            Color(0xFF1565C0)
                        )
                    }
                )
            )
    ) {
        // Particle background effect
        WeatherParticleBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo dengan animasi
            Image(
                painter = painterResource(id = R.drawable.halo_weather_icon),
                contentDescription = "Halo Weather Logo",
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        scaleX = 1f + (glowAlpha * 0.1f)
                        scaleY = 1f + (glowAlpha * 0.1f)
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Halo Weather",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = StackSansText,
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(0f, 4f),
                        blurRadius = 8f
                    )
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Cuaca Lokal Anda",
                fontSize = 16.sp,
                fontFamily = StackSansText,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Main Weather Card dengan glassmorphism effect
            when (val result = weatherResult.value) {
                is NetworkResponse.Success -> {
                    CurrentLocationCard(
                        data = result.data,
                        glowAlpha = glowAlpha,
                        onNavigateToSearch = onNavigateToSearch
                    )
                }
                NetworkResponse.Loading -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Mendeteksi lokasi Anda...",
                                    color = Color.White,
                                    fontFamily = StackSansText,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                is NetworkResponse.Error -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE).copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Tidak dapat mendeteksi lokasi",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828),
                                fontFamily = StackSansText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                result.message,
                                fontSize = 13.sp,
                                color = Color(0xFFC62828),
                                fontFamily = StackSansText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    // Idle state
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Search Button dengan animasi
            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Cari Kota Lain",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentLocationCard(
    data: WeatherModel,
    glowAlpha: Float,
    onNavigateToSearch: () -> Unit
) {
    val translatedCondition = WeatherTranslations.translate(data.current.condition.text)
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onNavigateToSearch() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Card dengan Location
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color(0xFF1565C0)
                } else {
                    Color(0xFF1E88E5)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        data.location.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                    Text(
                        data.location.country,
                        fontSize = 14.sp,
                        fontFamily = StackSansText,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Weather Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color.White.copy(alpha = 0.12f)
                } else {
                    Color.White.copy(alpha = 0.95f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Temperature
                Text(
                    "${data.current.temp_c.toInt()}°",
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color(0xFF1565C0),
                    fontFamily = StackSansText,
                    letterSpacing = (-4).sp
                )

                // Temperature Conversions
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val tempF = TemperatureUtils.celsiusToFahrenheit(data.current.temp_c)
                    val tempK = TemperatureUtils.celsiusToKelvin(data.current.temp_c)
                    val tempR = TemperatureUtils.celsiusToReamur(data.current.temp_c)

                    TempUnit(
                        value = TemperatureUtils.formatTemp(tempF),
                        unit = "°F",
                        isDark = isDark
                    )
                    Text(
                        "•",
                        color = if (isDark) Color.White.copy(alpha = 0.3f) else Color(0xFF1A1C1E).copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )
                    TempUnit(
                        value = TemperatureUtils.formatTemp(tempK),
                        unit = "K",
                        isDark = isDark
                    )
                    Text(
                        "•",
                        color = if (isDark) Color.White.copy(alpha = 0.3f) else Color(0xFF1A1C1E).copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )
                    TempUnit(
                        value = TemperatureUtils.formatTemp(tempR),
                        unit = "°R",
                        isDark = isDark
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Weather Icon
                AsyncImage(
                    model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    translatedCondition,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = StackSansText,
                    color = if (isDark) Color.White.copy(alpha = 0.9f) else Color(0xFF1A1C1E).copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Quick Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickInfoItemSimple(
                        icon = "💧",
                        label = "Kelembaban",
                        value = "${data.current.humidity}%",
                        isDark = isDark
                    )

                    QuickInfoItemSimple(
                        icon = "💨",
                        label = "Angin",
                        value = "${data.current.wind_kph.toInt()} km/h",
                        isDark = isDark
                    )

                    QuickInfoItemSimple(
                        icon = "👁️",
                        label = "Jarak Pandang",
                        value = "${data.current.vis_km} km",
                        isDark = isDark
                    )
                }
            }
        }
    }
}

@Composable
fun TempUnit(value: String, unit: String, isDark: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF1A1C1E).copy(alpha = 0.7f)
        )
        Text(
            unit,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF1A1C1E).copy(alpha = 0.5f)
        )
    }
}

@Composable
fun QuickInfoItemSimple(icon: String, label: String, value: String, isDark: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )
        Text(
            label,
            fontSize = 10.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF1A1C1E).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuickInfoItem(icon: String, label: String, value: String, isDark: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            icon,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )
        Text(
            label,
            fontSize = 10.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF1A1C1E).copy(alpha = 0.6f)
        )
    }
}

@Composable
fun WeatherParticleBackground() {
    val particles = remember {
        List(30) {
            Triple(
                Random.nextFloat(),  // xPos (0f..1f)
                Random.nextFloat(),  // initialY (0f..1f)
                Random.nextFloat() * 4f + 2f // radius
            )
        }
    }
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "particle_progress"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { (xPos, initialY, radius) ->
            val progress = (animationProgress + initialY) % 1f
            val yPos = progress * size.height
            val alpha = when {
                progress < 0.1f -> progress * 10
                progress > 0.9f -> (1 - progress) * 10
                else -> 1f
            }
            drawCircle(
                color = Color.White.copy(alpha = 0.25f * alpha),
                radius = radius,
                center = Offset(xPos * size.width, yPos)
            )
        }
    }
}

@Composable
fun WeatherSearchPage(viewModel: WeatherViewModel, onBackToHome: () -> Unit) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val locationSuggestions = viewModel.locationSuggestions.observeAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSuggestions by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    val hasSearched = weatherResult.value != null && weatherResult.value !is NetworkResponse.Idle
    val colors = MaterialTheme.colorScheme

    // Debounce untuk search suggestions
    LaunchedEffect(city) {
        if (city.length >= 2) {
            kotlinx.coroutines.delay(300)
            viewModel.searchLocationSuggestions(city)
        } else {
            viewModel.clearSuggestions()
        }
    }

    // Handle back press
    BackHandler(enabled = true) {
        if (hasSearched) {
            viewModel.resetWeatherResult()
            city = ""
        } else {
            onBackToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            Color(0xFF0A1929),
                            Color(0xFF1565C0),
                            Color(0xFF1E88E5)
                        )
                    } else {
                        listOf(
                            Color(0xFF42A5F5),
                            Color(0xFF1E88E5),
                            Color(0xFF1565C0)
                        )
                    }
                )
            )
    ) {
        // Particle background effect
        WeatherParticleBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (!hasSearched) Arrangement.Center else Arrangement.Top
        ) {

            if (!hasSearched) {
                // Logo untuk state idle
                Image(
                    painter = painterResource(id = R.drawable.halo_weather_icon),
                    contentDescription = "Halo Weather Logo",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Cari Kota",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = StackSansText,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))
            } else {
                Spacer(modifier = Modifier.height(40.dp))
            }

            // Search Box dengan Suggestions sebagai Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // Card Search Input
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { newValue ->
                                city = newValue
                                showSuggestions = newValue.length >= 2
                            },
                            modifier = Modifier.weight(1f),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_label),
                                    color = Color(0xFF1A1C1E).copy(alpha = 0.5f),
                                    fontFamily = StackSansText
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1E88E5),
                                focusedLabelColor = Color(0xFF1E88E5),
                                unfocusedBorderColor = Color(0xFF1A1C1E).copy(alpha = 0.2f),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = StackSansText,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        IconButton(
                            onClick = {
                                if (city.isNotEmpty()) {
                                    viewModel.getData(city)
                                    showSuggestions = false
                                    viewModel.clearSuggestions()
                                }
                                keyboardController?.hide()
                            },
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(56.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1E88E5),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }

                // Suggestions Dropdown - Positioned as Overlay
                if (showSuggestions && locationSuggestions.value.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 76.dp)
                            .heightIn(max = 250.dp)
                            .zIndex(2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            locationSuggestions.value.forEach { suggestion ->
                                SuggestionItem(
                                    suggestion = suggestion,
                                    onClick = {
                                        city = suggestion.name
                                        showSuggestions = false
                                        viewModel.getData(suggestion.name)
                                        viewModel.clearSuggestions()
                                        keyboardController?.hide()
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Crossfade(
                targetState = weatherResult.value,
                animationSpec = tween(500),
                label = "weather_crossfade"
            ) { result ->
                when (result) {

                    NetworkResponse.Idle -> {
                        // Idle state
                    }

                    NetworkResponse.Loading ->
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp
                        )

                    is NetworkResponse.Error ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    result.message,
                                    color = Color(0xFFC62828),
                                    fontFamily = StackSansText,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    is NetworkResponse.Success -> {
                        WeatherDetailsModern(result.data)
                    }

                    null -> {
                        // Null state
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionItem(
    suggestion: LocationSuggestion,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF1E88E5),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    suggestion.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = StackSansText,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    "${suggestion.region}${if (suggestion.region.isNotEmpty()) ", " else ""}${suggestion.country}",
                    fontSize = 12.sp,
                    fontFamily = StackSansText,
                    color = Color(0xFF1A1C1E).copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun WeatherDetailsModern(data: WeatherModel) {
    val translatedCondition = WeatherTranslations.translate(data.current.condition.text)

    val tempCelsius = data.current.temp_c
    val tempFahrenheit = TemperatureUtils.celsiusToFahrenheit(tempCelsius)
    val tempKelvin = TemperatureUtils.celsiusToKelvin(tempCelsius)
    val tempReamur = TemperatureUtils.celsiusToReamur(tempCelsius)

    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Card dengan Location
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color(0xFF1565C0)
                } else {
                    Color(0xFF1E88E5)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        data.location.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                    Text(
                        data.location.country,
                        fontSize = 14.sp,
                        fontFamily = StackSansText,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Weather Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color.White.copy(alpha = 0.12f)
                } else {
                    Color.White.copy(alpha = 0.95f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Temperature
                Text(
                    "${tempCelsius.toInt()}°",
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color(0xFF1565C0),
                    fontFamily = StackSansText,
                    letterSpacing = (-4).sp
                )

                // Temperature Conversions
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TempUnit(
                        value = TemperatureUtils.formatTemp(tempFahrenheit),
                        unit = "°F",
                        isDark = isDark
                    )
                    Text(
                        "•",
                        color = if (isDark) Color.White.copy(alpha = 0.3f) else Color(0xFF1A1C1E).copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )
                    TempUnit(
                        value = TemperatureUtils.formatTemp(tempKelvin),
                        unit = "K",
                        isDark = isDark
                    )
                    Text(
                        "•",
                        color = if (isDark) Color.White.copy(alpha = 0.3f) else Color(0xFF1A1C1E).copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )
                    TempUnit(
                        value = TemperatureUtils.formatTemp(tempReamur),
                        unit = "°R",
                        isDark = isDark
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Weather Icon
                AsyncImage(
                    model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    translatedCondition,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = StackSansText,
                    color = if (isDark) Color.White.copy(alpha = 0.9f) else Color(0xFF1A1C1E).copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Quick Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickInfoItemSimple(
                        icon = "💧",
                        label = "Kelembaban",
                        value = "${data.current.humidity}%",
                        isDark = isDark
                    )

                    QuickInfoItemSimple(
                        icon = "💨",
                        label = "Angin",
                        value = "${data.current.wind_kph.toInt()} km/h",
                        isDark = isDark
                    )

                    QuickInfoItemSimple(
                        icon = "👁️",
                        label = "Jarak Pandang",
                        value = "${data.current.vis_km} km",
                        isDark = isDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Additional Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color.White.copy(alpha = 0.12f)
                } else {
                    Color.White.copy(alpha = 0.95f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValModern("UV Index", data.current.uv.toString(), isDark)
                    WeatherKeyValModern("Presipitasi", "${data.current.precip_mm} mm", isDark)
                    WeatherKeyValModern("Tekanan", "${data.current.pressure_mb} mb", isDark)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValModern("Terasa Seperti", "${data.current.feelslike_c}°C", isDark)
                    WeatherKeyValModern("Waktu", data.location.localtime.split(" ")[1], isDark)
                    WeatherKeyValModern("Tanggal", data.location.localtime.split(" ")[0], isDark)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun WeatherKeyValModern(label: String, value: String, isDark: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(6.dp)
    ) {
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            fontSize = 11.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF1A1C1E).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherAmbience(condition: String) {
    when {
        condition.contains("rain", true) -> RainEffect()
        condition.contains("snow", true) -> SnowEffect()
        condition.contains("cloud", true) -> FogEffect()
        condition.contains("sun", true) || condition.contains("clear", true) -> SunGlowEffect()
    }
}

@Composable
fun RainEffect() {
    val drops = remember {
        List(60) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                3f + Random.nextFloat() * 2f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "rain")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rain_progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drops.forEach { (xPos, initialY, speed) ->
            val progress = (animationProgress + initialY) % 1f
            val yPos = progress * size.height

            drawLine(
                color = Color(0x6689CFF0),
                start = Offset(xPos * size.width, yPos),
                end = Offset(xPos * size.width, yPos + 25f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun SnowEffect() {
    val flakes = remember {
        List(40) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                2f + Random.nextFloat() * 1.5f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "snow")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "snow_progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        flakes.forEach { (xPos, initialY, flakeSize) ->
            val progress = (animationProgress + initialY) % 1f
            val yPos = progress * size.height

            drawCircle(
                color = Color(0xCCE3F2FD),
                radius = flakeSize,
                center = Offset(xPos * size.width, yPos)
            )
        }
    }
}

@Composable
fun FogEffect() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x2266A0CB), Color.Transparent)
            )
        )
    }
}

@Composable
fun SunGlowEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "sun")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "sun_glow"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 4
        val maxRadius = minOf(size.width, size.height) / 2.5f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x22FFEB3B),
                    Color(0x11FFC107),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = maxRadius * glow
            ),
            radius = maxRadius * glow,
            center = Offset(centerX, centerY)
        )
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    val colors = MaterialTheme.colorScheme

    val translatedCondition = WeatherTranslations.translate(data.current.condition.text)

    val tempCelsius = data.current.temp_c
    val tempFahrenheit = TemperatureUtils.celsiusToFahrenheit(tempCelsius)
    val tempKelvin = TemperatureUtils.celsiusToKelvin(tempCelsius)
    val tempReamur = TemperatureUtils.celsiusToReamur(tempCelsius)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1565C0),
                                Color(0xFF1976D2),
                                Color(0xFF1E88E5)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            data.location.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = StackSansText,
                            color = Color.White
                        )
                        Text(
                            data.location.country,
                            fontSize = 15.sp,
                            fontFamily = StackSansText,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${tempCelsius.toInt()}°",
                fontSize = 88.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontFamily = StackSansText,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = colors.primary.copy(alpha = 0.3f),
                        offset = Offset(0f, 4f),
                        blurRadius = 8f
                    )
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.primaryContainer.copy(alpha = 0.3f)
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TemperatureConversion(
                        value = TemperatureUtils.formatTemp(tempFahrenheit),
                        unit = "°F",
                        colors = colors
                    )

                    Text(
                        "•",
                        color = colors.onSurface.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )

                    TemperatureConversion(
                        value = TemperatureUtils.formatTemp(tempKelvin),
                        unit = "K",
                        colors = colors
                    )

                    Text(
                        "•",
                        color = colors.onSurface.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )

                    TemperatureConversion(
                        value = TemperatureUtils.formatTemp(tempReamur),
                        unit = "°R",
                        colors = colors
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = colors.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(140.dp)
        ) {
            AsyncImage(
                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            translatedCondition,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = StackSansText,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyVal(R.string.humidity, "${data.current.humidity}%")
                    WeatherKeyVal(R.string.wind, "${data.current.wind_kph} km/h")
                    WeatherKeyVal(R.string.uv_index, data.current.uv.toString())
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = colors.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyVal(R.string.precipitation, "${data.current.precip_mm} mm")
                    WeatherKeyVal(R.string.pressure, "${data.current.pressure_mb} mb")
                    WeatherKeyVal(R.string.feels_like, "${data.current.feelslike_c}°C")
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = colors.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyVal(R.string.visibility, "${data.current.vis_km} km")
                    WeatherKeyVal(R.string.time, data.location.localtime.split(" ")[1])
                    WeatherKeyVal(R.string.date, data.location.localtime.split(" ")[0])
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TemperatureConversion(
    value: String,
    unit: String,
    colors: ColorScheme
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = StackSansText,
            color = colors.onSurface.copy(alpha = 0.8f)
        )
        Text(
            unit,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = StackSansText,
            color = colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun WeatherKeyVal(key: Int, value: String) {
    val colors = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(6.dp)
    ) {
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = StackSansText,
            color = colors.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(id = key),
            fontSize = 11.sp,
            fontFamily = StackSansText,
            color = colors.onSurface.copy(alpha = 0.6f)
        )
    }
}