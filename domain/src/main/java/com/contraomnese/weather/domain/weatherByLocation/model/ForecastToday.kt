package com.contraomnese.weather.domain.weatherByLocation.model

data class ForecastToday(
    val maxTemperature: String,
    val minTemperature: String,
    val conditionCode: Int,
    val conditionText: String,
    val totalUvIndex: String,
    val rainChance: String,
    val totalRainFull: String,
    val sunrise: LocationTime?,
    val sunset: LocationTime?,
)