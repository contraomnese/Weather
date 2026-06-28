package com.contraomnese.weather.domain.app.model

import java.util.concurrent.TimeUnit

data class AppSettings(
    val language: Language = Language("en"),
    val timezone: String = "Europe/Moscow",
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.Kph,
    val precipitationUnit: PrecipitationUnit = PrecipitationUnit.Millimeters,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
    val pressureUnit: PressureUnit = PressureUnit.MmHg,
    val favoritesForecastUpdateEnabled: Boolean = false,
    val favoritesForecastNotificationEnabled: Boolean = false,
    val favoritesForecastUpdateInterval: Long = TimeUnit.SECONDS.toMillis(15),
)

@JvmInline
value class Language(val value: String) {
    init {
        require(isValid(value)) {
            "Invalid language code: '$value'. Expected ISO-639-1 lowercase code (e.g. 'en', 'ru')"
        }
    }

    companion object {
        private val regex = "^[a-z]{2}$".toRegex()

        fun isValid(code: String): Boolean =
            regex.matches(code)
    }
}

enum class WindSpeedUnit { Kph, Mph, Ms }
enum class PrecipitationUnit { Millimeters, Inches }
enum class TemperatureUnit { Celsius, Fahrenheit }
enum class PressureUnit { MmHg, GPa }
