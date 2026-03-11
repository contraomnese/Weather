package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.errors.INetworkError
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

interface INetworkParser {

    val converterFactory: Converter.Factory
    val retrofit: Retrofit

    fun <T : Any> parseOrThrowError(response: Response<T>): T
    fun <T : INetworkError> buildException(parsed: T?, code: Int): Exception

}