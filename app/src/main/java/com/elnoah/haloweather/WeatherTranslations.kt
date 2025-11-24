package com.elnoah.haloweather

object WeatherTranslations {
    private val translations = mapOf(

        "sunny" to "Cerah",
        "clear" to "Cerah",
        "partly cloudy" to "Berawan Sebagian",
        "fair" to "Cerah",


        "cloudy" to "Berawan",
        "overcast" to "Mendung",
        "mostly cloudy" to "Kebanyakan Berawan",


        "rain" to "Hujan",
        "light rain" to "Hujan Ringan",
        "moderate rain" to "Hujan Sedang",
        "heavy rain" to "Hujan Lebat",
        "light rain shower" to "Hujan Rintik-rintik",
        "moderate or heavy rain shower" to "Hujan Sedang atau Lebat",
        "torrential rain shower" to "Hujan Deras",
        "patchy rain possible" to "Kemungkinan Hujan",
        "patchy rain nearby" to "Hujan di Sekitar",
        "light freezing rain" to "Hujan Beku Ringan",
        "moderate or heavy freezing rain" to "Hujan Beku Sedang atau Lebat",


        "drizzle" to "Gerimis",
        "light drizzle" to "Gerimis Ringan",
        "patchy light drizzle" to "Gerimis Ringan Sebagian",
        "freezing drizzle" to "Gerimis Beku",
        "heavy freezing drizzle" to "Gerimis Beku Lebat",


        "mist" to "Kabut Tipis",
        "fog" to "Kabut",
        "freezing fog" to "Kabut Beku",
        "patchy fog" to "Kabut Sebagian",

        "thundery outbreaks possible" to "Kemungkinan Petir",
        "thunderstorm" to "Badai Petir",
        "moderate or heavy rain with thunder" to "Hujan Sedang atau Lebat Disertai Petir",
        "patchy light rain with thunder" to "Hujan Ringan Disertai Petir",
        "patchy light snow with thunder" to "Salju Ringan Disertai Petir",
        "moderate or heavy snow with thunder" to "Salju Sedang atau Lebat Disertai Petir",


        "snow" to "Salju",
        "light snow" to "Salju Ringan",
        "moderate snow" to "Salju Sedang",
        "heavy snow" to "Salju Lebat",
        "patchy light snow" to "Salju Ringan Sebagian",
        "patchy moderate snow" to "Salju Sedang Sebagian",
        "patchy heavy snow" to "Salju Lebat Sebagian",
        "light snow showers" to "Hujan Salju Ringan",
        "moderate or heavy snow showers" to "Hujan Salju Sedang atau Lebat",
        "blizzard" to "Badai Salju",
        "blowing snow" to "Salju Bertiup",


        "sleet" to "Hujan Es",
        "light sleet" to "Hujan Es Ringan",
        "moderate or heavy sleet" to "Hujan Es Sedang atau Lebat",
        "light sleet showers" to "Hujan Es Ringan",
        "moderate or heavy sleet showers" to "Hujan Es Sedang atau Lebat",


        "ice pellets" to "Hujan Es",
        "light showers of ice pellets" to "Hujan Es Ringan",
        "moderate or heavy showers of ice pellets" to "Hujan Es Sedang atau Lebat",


        "windy" to "Berangin"
    )

    /**
     * Translate weather condition based on system language
     * @param condition Original weather condition from API
     * @param locale System locale (e.g., "id" for Indonesian, "en" for English)
     * @return Translated condition if locale is Indonesian, original if English
     */

    fun translate(condition: String, locale: String = java.util.Locale.getDefault().language): String {

        if (locale != "id" && locale != "in") {
            android.util.Log.d("WeatherTranslations", "System locale: $locale - Using original English: '$condition'")
            return condition
        }

        val normalizedCondition = condition.trim().lowercase()

        android.util.Log.d("WeatherTranslations", "System locale: $locale - Translating to Indonesian")
        android.util.Log.d("WeatherTranslations", "Original: '$condition' | Normalized: '$normalizedCondition'")


        val exactMatch = translations[normalizedCondition]
        if (exactMatch != null) {
            android.util.Log.d("WeatherTranslations", "✓ Exact match found: '$exactMatch'")
            return exactMatch
        }


        for ((key, value) in translations) {
            if (normalizedCondition.contains(key)) {
                android.util.Log.d("WeatherTranslations", "✓ Partial match found: '$value' (key: '$key')")
                return value
            }
        }


        android.util.Log.w("WeatherTranslations", "✗ No translation found, using original")
        return condition
    }
}