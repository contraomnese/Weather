package com.contraomnese.weather.data.network.models.errors

data class LocationsErrorResponse(
    override val error: NetworkError?,
) : INetworkError