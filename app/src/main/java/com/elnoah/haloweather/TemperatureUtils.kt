package com.elnoah.haloweather

object TemperatureUtils {

    fun celsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9/5) + 32
    }

    fun celsiusToKelvin(celsius: Double): Double {
        return celsius + 273.15
    }

    fun celsiusToReamur(celsius: Double): Double {
        return celsius * 4/5
    }

    fun formatTemp(value: Double, decimals: Int = 1): String {
        return String.format("%.${decimals}f", value)
    }
}