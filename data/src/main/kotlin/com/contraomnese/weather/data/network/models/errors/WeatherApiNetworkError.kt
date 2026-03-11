package com.contraomnese.weather.data.network.models.errors

interface WeatherApiNetworkError {
    val code: Int?
    val message: String?
}