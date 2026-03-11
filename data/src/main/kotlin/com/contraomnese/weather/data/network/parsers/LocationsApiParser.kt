package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.errors.BaseNetworkParser
import com.contraomnese.weather.data.network.models.errors.LocationsErrorResponse
import retrofit2.Converter
import retrofit2.Retrofit

class LocationsApiParser(
    override val converterFactory: Converter.Factory,
    override val retrofit: Retrofit,
) : BaseNetworkParser<LocationsErrorResponse>(LocationsErrorResponse::class.java) {

    override val unauthorizedCodes: Set<Int> = setOf(401, 403)
    override val badRequestCodes: Set<Int> = setOf(400)
    override val notFoundCodes: Set<Int> = setOf(404)
    override val rateLimitCodes: Set<Int> = setOf(429)
    override val unavailableCodes: Set<Int> = setOf(500)

    override fun provideApiCode(parsed: LocationsErrorResponse?): Int? = null

    override fun provideMessage(parsed: LocationsErrorResponse?): String? =
        parsed?.error
}