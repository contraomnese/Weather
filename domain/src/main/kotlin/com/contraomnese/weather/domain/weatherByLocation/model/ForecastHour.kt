package com.contraomnese.weather.domain.weatherByLocation.model

data class ForecastHour(
    val time: String,
    val temperature: String,
    val condition: WeatherCondition,
    val isDay: Boolean,
)