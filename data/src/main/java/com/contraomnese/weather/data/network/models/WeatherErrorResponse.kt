package com.contraomnese.weather.data.network.models

data class WeatherErrorResponse(
    val error: WeatherErrorDetail,
)

data class WeatherErrorDetail(
    val code: Int,
    val message: String,
)