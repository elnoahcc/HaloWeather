package com.elnoah.haloweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elnoah.haloweather.api.Constant
import com.elnoah.haloweather.api.NetworkResponse
import com.elnoah.haloweather.api.RetrofitInstance
import com.elnoah.haloweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

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
                    // Handle different error codes
                    val errorMessage = when (response.code()) {
                        400 -> "Lokasi tidak valid. Coba masukkan nama kota yang lebih umum."
                        401 -> "API Key tidak valid."
                        403 -> "Akses ditolak. Periksa API key Anda."
                        404 -> "Lokasi '$city' tidak ditemukan. Coba nama kota yang lebih besar atau tambahkan nama negara (contoh: Solo, Indonesia)"
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

    fun resetWeatherResult() {
        _weatherResult.value = null
    }
}
