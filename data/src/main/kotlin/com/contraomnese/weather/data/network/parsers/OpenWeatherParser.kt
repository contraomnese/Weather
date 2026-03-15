package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.errors.BaseNetworkParser
import com.contraomnese.weather.data.network.models.errors.OpenWeatherErrorResponse
import retrofit2.Converter
import retrofit2.Retrofit

class OpenWeatherParser(
    override val converterFactory: Converter.Factory,
    override val retrofit: Retrofit,
) : BaseNetworkParser<OpenWeatherErrorResponse>(OpenWeatherErrorResponse::class.java) {

    override val unauthorizedCodes: Set<Int> = setOf(401, 403)
    override val badRequestCodes: Set<Int> = setOf(400)
    override val notFoundCodes: Set<Int> = setOf(404)
    override val rateLimitCodes: Set<Int> = setOf(429)
    override val unavailableCodes: Set<Int> = setOf(500)

    override fun provideApiCode(parsed: OpenWeatherErrorResponse?): Int? = null

    override fun provideMessage(parsed: OpenWeatherErrorResponse?): String? =
        parsed?.reason
}