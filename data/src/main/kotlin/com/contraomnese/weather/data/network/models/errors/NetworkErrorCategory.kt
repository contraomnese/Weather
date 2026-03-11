package com.contraomnese.weather.data.network.models.errors

sealed class NetworkErrorCategory {
    object Unauthorized : NetworkErrorCategory()
    object BadRequest : NetworkErrorCategory()
    object NotFound : NetworkErrorCategory()
    object Unavailable : NetworkErrorCategory()
    object RateLimits : NetworkErrorCategory()
    object Unknown : NetworkErrorCategory()
}