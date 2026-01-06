package com.contraomnese.weather.domain.weatherByLocation.model

data class ForecastDay(
    val dayNumber: String,
    val dayName: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val condition: WeatherCondition,
    val totalRainFull: Int,
)