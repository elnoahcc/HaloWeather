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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        var city by remember { mutableStateOf("") }
        val weatherResult = viewModel.weatherResult.observeAsState()
        val locationSuggestions = viewModel.locationSuggestions.observeAsState(emptyList())
        val keyboardController = LocalSoftwareKeyboardController.current
        var backPressedOnce by remember { mutableStateOf(false) }
        var showSuggestions by remember { mutableStateOf(false) }

        val hasSearched = weatherResult.value != null
        val colors = MaterialTheme.colorScheme

        // Handle back press
        BackHandler(enabled = true) {
            if (hasSearched) {
                viewModel.resetWeatherResult()
                city = ""
                backPressedOnce = false
            } else if (backPressedOnce) {
                System.exit(0)
            } else {
                backPressedOnce = true
            }
        }

        // Reset backPressedOnce setelah 2 detik
        LaunchedEffect(backPressedOnce) {
            if (backPressedOnce && !hasSearched) {
                kotlinx.coroutines.delay(2000)
                backPressedOnce = false
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (!hasSearched) Arrangement.Center else Arrangement.Top
            ) {

                AnimatedContent(
                    targetState = hasSearched,
                    transitionSpec = {
                        fadeIn(tween(700)) + scaleIn(initialScale = 0.9f) togetherWith
                                fadeOut(tween(400)) + scaleOut(targetScale = 0.7f)
                    }, label = "logo_animation"
                ) { searched ->
                    Image(
                        painter = painterResource(id = R.drawable.halo_weather_icon),
                        contentDescription = "Halo Weather Logo",
                        modifier = Modifier
                            .size(if (searched) 70.dp else 180.dp)
                            .padding(top = if (searched) 30.dp else 0.dp)
                    )
                }

                Spacer(modifier = Modifier.height(if (!hasSearched) 32.dp else 20.dp))

                // Search Box dengan Suggestions
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = if (!hasSearched) 20.dp else 0.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colors.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                        showSuggestions = newValue.isNotEmpty()
                                        viewModel.searchLocationSuggestions(newValue)
                                    },
                                    modifier = Modifier.weight(1f),
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.search_label),
                                            color = colors.onSurface.copy(alpha = 0.5f),
                                            fontFamily = StackSansText
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colors.primary,
                                        focusedLabelColor = colors.primary,
                                        unfocusedBorderColor = colors.onSurface.copy(alpha = 0.2f)
                                    ),
                                    singleLine = true,
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontFamily = StackSansText
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
                                        color = colors.primary,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = "Search",
                                            tint = colors.onPrimary,
                                            modifier = Modifier.padding(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Suggestions Dropdown
                        if (showSuggestions && locationSuggestions.value.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colors.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 200.dp)
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
                }

                Spacer(modifier = Modifier.height(24.dp))

                Crossfade(
                    targetState = weatherResult.value,
                    animationSpec = tween(700),
                    label = "weather_crossfade"
                ) { result ->
                    when (result) {

                        NetworkResponse.Loading ->
                            CircularProgressIndicator(
                                color = colors.primary,
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
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        result.message,
                                        color = Color(0xFFC62828),
                                        fontFamily = StackSansText,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                        is NetworkResponse.Success -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                WeatherAmbience(result.data.current.condition.text)
                                WeatherDetails(result.data)
                            }
                        }

                        null -> {
                            if (backPressedOnce) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = colors.primaryContainer
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Tekan sekali lagi untuk keluar",
                                        color = colors.onPrimaryContainer,
                                        fontFamily = StackSansText,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
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
    val colors = MaterialTheme.colorScheme

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
                tint = colors.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    suggestion.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = StackSansText,
                    color = colors.onSurface
                )
                Text(
                    "${suggestion.region}${if (suggestion.region.isNotEmpty()) ", " else ""}${suggestion.country}",
                    fontSize = 12.sp,
                    fontFamily = StackSansText,
                    color = colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
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
            .padding(vertical = 8.dp),
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