package com.contraomnese.weather.data.network.models.errors

data class OpenWeatherErrorResponse(
    val error: Boolean,
    val reason: String,
) : INetworkError