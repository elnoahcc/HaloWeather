package com.elnoah.haloweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.elnoah.haloweather.ui.theme.HaloWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ cara benar ambil ViewModel
        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            HaloWeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ✅ kirim viewModel ke composable
                    WeatherPage(viewModel = weatherViewModel)
                }
            }
        }
    }
}
