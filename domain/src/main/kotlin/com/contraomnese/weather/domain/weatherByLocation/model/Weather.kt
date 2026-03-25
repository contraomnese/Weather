package com.contraomnese.weather.domain.weatherByLocation.model

data class Weather(
    val temperature: String,
    val feelsLikeTemperature: String,
    val isDay: Boolean,
    val condition: WeatherCondition,
    val airQuality: AirQuality,
    val uvIndex: UvIndex,
    val windSpeed: String,
    val windDegree: Int,
    val gustSpeed: String,
    val humidity: Int,
    val dewPoint: Int,
    val pressure: Int,
    val willRain: Boolean,
    val rainfallBeforeNow: List<Double>,
    val rainfallAfterNow: List<Double>,
    val rainfallNow: Double,
    val maxRainfall: Double,
)
