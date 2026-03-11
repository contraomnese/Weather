package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.errors.BaseNetworkParser
import com.contraomnese.weather.data.network.models.errors.WeatherErrorResponse
import retrofit2.Converter
import retrofit2.Retrofit

class WeatherApiParser(
    override val converterFactory: Converter.Factory,
    override val retrofit: Retrofit,
) : BaseNetworkParser<WeatherErrorResponse>(WeatherErrorResponse::class.java) {

    override val unauthorizedCodes: Set<Int> = setOf(1002, 2006, 2007, 2008, 2009)
    override val badRequestCodes: Set<Int> = setOf(1003, 1005, 9000, 9001)
    override val notFoundCodes: Set<Int> = setOf(1006)
    override val unavailableCodes: Set<Int> = setOf(9999)
    override val rateLimitCodes: Set<Int> = setOf()

    override fun provideApiCode(parsed: WeatherErrorResponse?): Int? =
        parsed?.error?.code

    override fun provideMessage(parsed: WeatherErrorResponse?): String? =
        parsed?.error?.message
}