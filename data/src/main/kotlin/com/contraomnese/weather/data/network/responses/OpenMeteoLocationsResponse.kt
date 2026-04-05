package com.contraomnese.weather.data.network.responses

import com.contraomnese.weather.data.network.models.openmeteo.geolocation.LocationNetwork

data class OpenMeteoLocationsResponse(
    val results: List<LocationNetwork>,
)