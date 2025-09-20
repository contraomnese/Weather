package com.contraomnese.weather.domain.weatherByLocation.model.internal

data class ForecastHour(
    val time: String,
    val temperature: String,
    val condition: CompactWeatherCondition,
    val isDay: Boolean,
)