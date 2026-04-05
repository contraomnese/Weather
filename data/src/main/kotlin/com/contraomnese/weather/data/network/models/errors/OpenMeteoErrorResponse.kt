package com.contraomnese.weather.data.network.models.errors

data class OpenMeteoErrorResponse(
    val error: Boolean,
    val reason: String,
) : INetworkError