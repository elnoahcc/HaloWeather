package com.elnoah.haloweather

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elnoah.haloweather.api.Constant
import com.elnoah.haloweather.api.LocationSuggestion
import com.elnoah.haloweather.api.NetworkResponse
import com.elnoah.haloweather.api.RetrofitInstance
import com.elnoah.haloweather.api.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherApi = RetrofitInstance.weatherApi

    private val _weatherResult =
        MutableLiveData<NetworkResponse<WeatherModel>>(NetworkResponse.Idle)
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    private val _locationSuggestions = MutableLiveData<List<LocationSuggestion>>()
    val locationSuggestions: LiveData<List<LocationSuggestion>> = _locationSuggestions

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _locationDisabled = MutableStateFlow(false)
    val locationDisabled: StateFlow<Boolean> = _locationDisabled

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = weatherApi.getForecastWeather(
                    Constant.apiKey,
                    city = city,
                    days = 3
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                _weatherResult.value =
                    NetworkResponse.Error("Failed to load data: ${e.message}")
            }
        }
    }

    fun getCurrentLocationWeather() {
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _locationDisabled.value = true
            return
        }

        _weatherResult.value = NetworkResponse.Loading

        try {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getWeatherByCoordinates(location.latitude, location.longitude)
                } else {
                    _locationDisabled.value = true
                }
            }.addOnFailureListener {
                _locationDisabled.value = true
            }
        } catch (e: Exception) {
            _locationDisabled.value = true
        }
    }

    private fun getWeatherByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getForecastWeather(  // ← PERBAIKAN
                    Constant.apiKey,
                    city = "$lat,$lon",  // Parameter tetap bisa pakai koordinat
                    days = 3
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to load data: ${e.message}")
            }
        }
    }

    fun searchLocationSuggestions(query: String) {
        if (query.length < 2) {
            _locationSuggestions.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val response = weatherApi.searchLocation(
                    Constant.apiKey,
                    query
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _locationSuggestions.value = it
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    fun clearSuggestions() {
        _locationSuggestions.value = emptyList()
    }

    fun resetWeatherResult() {
        _weatherResult.value = NetworkResponse.Idle
    }
}
