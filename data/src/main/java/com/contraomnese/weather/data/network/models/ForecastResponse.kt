package com.contraomnese.weather.data.network.models

data class ForecastResponse(
    val location: ForecastLocationNetwork,
    val current: ForecastCurrentNetwork,
    val forecast: ForecastNetwork,
    val alerts: ForecastAlertsWeatherNetwork,
)