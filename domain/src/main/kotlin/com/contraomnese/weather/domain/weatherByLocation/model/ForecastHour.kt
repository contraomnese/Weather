package com.contraomnese.weather.domain.weatherByLocation.model

data class ForecastHour(
    val time: LocationTime,
    val temperature: String = "",
    val condition: WeatherCondition,
    val isDay: Boolean = false,
    val precipitationProbability: Int = 0,
)