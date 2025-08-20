package com.contraomnese.weather.domain.weatherByLocation.model

data class WeatherDomainModel(
    val condition: String,
    val currentTemperature: String,
    val maxTemperature: String,
    val minTemperature: String,
)
