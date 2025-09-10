package com.contraomnese.weather.domain.app.model


data class AppSettings(
    val language: Language = Language.English,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.Kph,
    val precipitationUnit: PrecipitationUnit = PrecipitationUnit.Millimeters,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
    val visibilityUnit: VisibilityUnit = VisibilityUnit.Kilometers,
    val pressureUnit: PressureUnit = PressureUnit.MmHg,
)

enum class Language {
    English,
    Russian;

    companion object {
        fun fromLocaleCode(code: String): Language =
            when (code.lowercase()) {
                "ru" -> Russian
                "en" -> English
                else -> English
            }

        fun Language.toLocalCode(): String = when (this) {
            English -> "en"
            Russian -> "ru"
        }
    }
}

enum class WindSpeedUnit { Kph, Mph, Ms }
enum class PrecipitationUnit { Millimeters, Inches }
enum class TemperatureUnit { Celsius, Fahrenheit }
enum class VisibilityUnit { Kilometers, Miles }
enum class PressureUnit { MmHg, InchHg }
