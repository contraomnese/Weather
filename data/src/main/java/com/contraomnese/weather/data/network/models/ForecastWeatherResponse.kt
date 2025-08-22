package com.contraomnese.weather.data.network.models

data class ForecastWeatherResponse(
    val location: LocationWeatherNetwork,
    val current: CurrentWeatherNetwork,
    val forecast: ForecastWeatherNetwork,
    val alerts: AlertsWeatherNetwork,
)