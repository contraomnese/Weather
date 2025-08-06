package com.contraomnese.weather.data.network.models

data class CurrentWeatherResponse(
    val location: LocationWeatherNetwork,
    val current: CurrentWeatherNetwork,
)

data class ErrorResponse(
    val error: ErrorDetail,
)

data class ErrorDetail(
    val code: Int,
    val message: String,
)