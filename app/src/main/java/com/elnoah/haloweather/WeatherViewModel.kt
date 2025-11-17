package com.elnoah.haloweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elnoah.haloweather.api.Constant
import com.elnoah.haloweather.api.LocationSuggestion
import com.elnoah.haloweather.api.NetworkResponse
import com.elnoah.haloweather.api.RetrofitInstance
import com.elnoah.haloweather.api.WeatherModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    // TAMBAHKAN INI - LiveData untuk suggestions
    private val _locationSuggestions = MutableLiveData<List<LocationSuggestion>>()
    val locationSuggestions: LiveData<List<LocationSuggestion>> = _locationSuggestions

    private var searchJob: Job? = null

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Lokasi tidak valid. Coba masukkan nama kota yang lebih umum."
                        401 -> "API Key tidak valid."
                        403 -> "Akses ditolak. Periksa API key Anda."
                        404 -> "Lokasi '$city' tidak ditemukan. Coba nama kota yang lebih besar."
                        else -> "Gagal mengambil data cuaca (Error ${response.code()})"
                    }
                    _weatherResult.value = NetworkResponse.Error(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "Tidak ada koneksi internet"
                    is java.net.SocketTimeoutException -> "Koneksi timeout. Periksa internet Anda."
                    else -> "Gagal memuat data: ${e.localizedMessage}"
                }
                _weatherResult.value = NetworkResponse.Error(errorMessage)
            }
        }
    }

    // TAMBAHKAN INI - Search dengan debounce
    fun searchLocationSuggestions(query: String) {
        // Cancel previous search
        searchJob?.cancel()

        if (query.length < 3) {
            _locationSuggestions.value = emptyList()
            return
        }

        // Debounce 500ms
        searchJob = viewModelScope.launch {
            delay(500)
            try {
                val response = weatherApi.searchLocation(Constant.apiKey, query)
                if (response.isSuccessful) {
                    _locationSuggestions.value = response.body() ?: emptyList()
                } else {
                    _locationSuggestions.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Search error: ${e.message}")
                _locationSuggestions.value = emptyList()
            }
        }
    }

    fun clearSuggestions() {
        _locationSuggestions.value = emptyList()
    }

    fun resetWeatherResult() {
        _weatherResult.value = null
    }
}