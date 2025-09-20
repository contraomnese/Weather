package com.contraomnese.weather.domain.weatherByLocation.model.internal

data class ForecastDay(
    val dayNumber: String,
    val dayName: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val condition: CompactWeatherCondition,
    val totalRainFull: Int,
)