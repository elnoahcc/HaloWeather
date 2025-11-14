package com.elnoah.haloweather

object WeatherTranslations {

    private val weatherConditions = mapOf(
        // Clear
        "sunny" to "Cerah",
        "clear" to "Cerah",

        // Clouds
        "partly cloudy" to "Berawan Sebagian",
        "cloudy" to "Berawan",
        "overcast" to "Mendung",
        "mist" to "Berkabut",
        "fog" to "Kabut",
        "foggy" to "Berkabut",

        // Rain
        "patchy rain possible" to "Kemungkinan Hujan",
        "patchy rain nearby" to "Hujan di Sekitar",
        "patchy light drizzle" to "Gerimis Ringan Sebagian",
        "light drizzle" to "Gerimis",
        "freezing drizzle" to "Gerimis Beku",
        "heavy freezing drizzle" to "Gerimis Beku Lebat",
        "patchy light rain" to "Hujan Ringan Sebagian",
        "light rain" to "Hujan Ringan",
        "moderate rain at times" to "Hujan Sedang",
        "moderate rain" to "Hujan Sedang",
        "heavy rain at times" to "Hujan Lebat",
        "heavy rain" to "Hujan Lebat",
        "light rain shower" to "Hujan Rintik",
        "moderate or heavy rain shower" to "Hujan Deras",
        "torrential rain shower" to "Hujan Sangat Deras",
        "light freezing rain" to "Hujan Beku Ringan",
        "moderate or heavy freezing rain" to "Hujan Beku Lebat",

        // Snow
        "patchy snow possible" to "Kemungkinan Salju",
        "blowing snow" to "Salju Bertiup",
        "blizzard" to "Badai Salju",
        "patchy light snow" to "Salju Ringan Sebagian",
        "light snow" to "Salju Ringan",
        "patchy moderate snow" to "Salju Sedang Sebagian",
        "moderate snow" to "Salju Sedang",
        "patchy heavy snow" to "Salju Lebat Sebagian",
        "heavy snow" to "Salju Lebat",
        "light snow showers" to "Hujan Salju Ringan",
        "moderate or heavy snow showers" to "Hujan Salju Lebat",

        // Sleet
        "patchy sleet possible" to "Kemungkinan Hujan Es",
        "light sleet" to "Hujan Es Ringan",
        "moderate or heavy sleet" to "Hujan Es Lebat",
        "light sleet showers" to "Hujan Es Ringan",
        "moderate or heavy sleet showers" to "Hujan Es Lebat",

        // Ice Pellets
        "ice pellets" to "Butiran Es",
        "light showers of ice pellets" to "Hujan Butiran Es Ringan",
        "moderate or heavy showers of ice pellets" to "Hujan Butiran Es Lebat",

        // Thunder
        "thundery outbreaks possible" to "Kemungkinan Petir",
        "patchy light rain with thunder" to "Hujan Ringan Disertai Petir",
        "moderate or heavy rain with thunder" to "Hujan Lebat Disertai Petir",
        "patchy light snow with thunder" to "Salju Ringan Disertai Petir",
        "moderate or heavy snow with thunder" to "Salju Lebat Disertai Petir",

        // Freezing Fog
        "patchy freezing drizzle possible" to "Kemungkinan Gerimis Beku",
        "freezing fog" to "Kabut Beku"
    )

    /**
     * Translate English weather condition to Indonesian
     * @param condition Weather condition text from API
     * @return Translated condition or original if not found
     */
    fun translate(condition: String): String {
        val lowerCondition = condition.lowercase().trim()
        return weatherConditions[lowerCondition] ?: condition
    }

    /**
     * Check if translation exists
     */
    fun hasTranslation(condition: String): Boolean {
        return weatherConditions.containsKey(condition.lowercase().trim())
    }
}