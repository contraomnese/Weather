package com.contraomnese.weather.domain.app.model



data class AppSettings(
    val language: Language = Language("en"),
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.Kph,
    val precipitationUnit: PrecipitationUnit = PrecipitationUnit.Millimeters,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
    val pressureUnit: PressureUnit = PressureUnit.MmHg,
)

@JvmInline
value class Language(val value: String)

enum class WindSpeedUnit { Kph, Mph, Ms }
enum class PrecipitationUnit { Millimeters, Inches }
enum class TemperatureUnit { Celsius, Fahrenheit }
enum class PressureUnit { MmHg, GPa }
