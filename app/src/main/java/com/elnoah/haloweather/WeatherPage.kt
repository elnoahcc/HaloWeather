    package com.elnoah.haloweather

    import androidx.compose.animation.*
    import androidx.compose.animation.core.*
    import androidx.compose.foundation.Canvas
    import androidx.compose.foundation.Image
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
    import androidx.compose.ui.geometry.Offset
    import androidx.compose.ui.graphics.*
    import androidx.compose.ui.platform.LocalSoftwareKeyboardController
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import coil.compose.AsyncImage
    import com.elnoah.haloweather.api.NetworkResponse
    import com.elnoah.haloweather.api.WeatherModel
    import com.elnoah.haloweather.ui.theme.StackSansText
    import com.elnoah.haloweather.ui.theme.Typography
    import kotlin.random.Random

    private val BlueLightScheme = lightColorScheme(
        primary = Color(0xFF1E88E5),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFD0E8FF),
        onPrimaryContainer = Color(0xFF00315A),
        background = Color.White,
        onBackground = Color(0xFF0A0A0A)
    )

    private val BlueDarkScheme = darkColorScheme(
        primary = Color(0xFF90CAF9),
        onPrimary = Color(0xFF001E3C),
        primaryContainer = Color(0xFF00315A),
        onPrimaryContainer = Color(0xFFD0E8FF),
        background = Color(0xFF0B0B0B),
        onBackground = Color(0xFFE6E6E6)
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
            val keyboardController = LocalSoftwareKeyboardController.current

            val hasSearched = weatherResult.value != null
            val colors = MaterialTheme.colorScheme

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colors.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (!hasSearched) Arrangement.Center else Arrangement.Top
                ) {

                    AnimatedContent(
                        targetState = hasSearched,
                        transitionSpec = {
                            fadeIn(tween(700)) + scaleIn(initialScale = 0.9f) togetherWith
                                    fadeOut(tween(400)) + scaleOut(targetScale = 0.7f)
                        }
                    ) { searched ->
                        Image(
                            painter = painterResource(id = R.drawable.halo_weather_icon),
                            contentDescription = "Halo Weather Logo",
                            modifier = Modifier
                                .size(if (searched) 80.dp else 190.dp)
                                .padding(top = if (searched) 40.dp else 0.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(if (!hasSearched) 24.dp else 16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (!hasSearched) 40.dp else 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.primary,
                                focusedLabelColor = colors.primary
                            )
                        )
                        IconButton(
                            onClick = {
                                if (city.isNotEmpty())
                                    viewModel.getData(city)
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(Icons.Default.Search, null, tint = colors.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Crossfade(targetState = weatherResult.value, animationSpec = tween(700)) { result ->
                        when (result) {

                            NetworkResponse.Loading ->
                                CircularProgressIndicator(color = colors.primary)

                            is NetworkResponse.Error ->
                                Text(result.message, color = Color.Red)

                            is NetworkResponse.Success -> {
                                Box(modifier = Modifier.fillMaxSize()) {

                                    WeatherAmbience(result.data.current.condition.text)

                                    WeatherDetails(result.data)
                                }
                            }

                            null -> {}
                        }
                    }
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
        val drops = remember { List(70) { Pair(Random.nextFloat(), Random.nextFloat()) } }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drops.forEachIndexed { index, drop ->
                val speed = 8 + (index % 5)
                val yOffset = ((System.currentTimeMillis() / speed) % size.height)

                drawLine(
                    color = Color(0x5589CFF0),
                    start = Offset(drop.first * size.width, yOffset),
                    end = Offset(drop.first * size.width, yOffset + 18),
                    strokeWidth = 3f
                )
            }
        }
    }

    @Composable
    fun SnowEffect() {
        val flakes = remember { List(50) { Pair(Random.nextFloat(), Random.nextFloat()) } }

        Canvas(modifier = Modifier.fillMaxSize()) {
            flakes.forEachIndexed { index, flake ->
                val speed = 15 + (index % 3)
                val yOffset = ((System.currentTimeMillis() / speed) % size.height)

                drawCircle(
                    color = Color(0xAAE3F2FD),
                    radius = 4f,
                    center = Offset(flake.first * size.width, yOffset)
                )
            }
        }
    }

    @Composable
    fun FogEffect() {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0x3366A0CB), Color.Transparent)
                )
            )
        }
    }

    @Composable
    fun SunGlowEffect() {
        val glow by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(2500, easing = FastOutSlowInEasing)
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x33BBDEFB), Color.Transparent),
                    center = Offset(size.width / 2, size.height / 3),
                    radius = 500f * glow
                ),
                radius = 500f * glow
            )
        }
    }

    @Composable
    fun WeatherDetails(data: WeatherModel) {
        val colors = MaterialTheme.colorScheme
        val dark = isSystemInDarkTheme()

        val detailsCardColor =
            if (dark) Color(0xFF0D1B2A) else Color(0xFFBBDEFB)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0D47A1)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            data.location.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = StackSansText,
                            color = Color.White
                        )
                        Text(
                            data.location.country,
                            fontSize = 14.sp,
                            fontFamily = StackSansText,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "${data.current.temp_c}°C",
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontFamily = StackSansText
            )

            AsyncImage(
                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                data.current.condition.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = StackSansText,
                color = colors.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = detailsCardColor
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherKeyVal(R.string.humidity, "${data.current.humidity}%")
                        WeatherKeyVal(R.string.wind, "${data.current.wind_kph} km/h")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherKeyVal(R.string.uv_index, data.current.uv.toString())
                        WeatherKeyVal(R.string.precipitation, "${data.current.precip_mm} mm")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherKeyVal(R.string.time, data.location.localtime.split(" ")[1])
                        WeatherKeyVal(R.string.date, data.location.localtime.split(" ")[0])
                    }
                }
            }
        }
    }

    @Composable
    fun WeatherKeyVal(key: Int, value: String) {
        val colors = MaterialTheme.colorScheme

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = StackSansText,
                color = colors.onPrimaryContainer
            )
            Text(
                text = stringResource(id = key),
                fontSize = 13.sp,
                fontFamily = StackSansText,
                color = colors.onPrimaryContainer.copy(alpha = 0.7f)
            )

        }
    }
