package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.errors.INetworkError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

interface INetworkParser {

    val converterFactory: Converter.Factory
    val retrofit: Retrofit

    fun <T : Any> parseOrThrowError(response: Response<T>): T
    fun <T : INetworkError> buildException(parsed: T?, code: Int): Exception

}

inline fun <reified T> INetworkParser.parseError(errorBody: ResponseBody?): T? {
    if (errorBody == null) return null
    return try {
        val converter: Converter<ResponseBody, T> =
            retrofit.responseBodyConverter(T::class.java, emptyArray())
        converter.convert(errorBody)
    } catch (e: Exception) {
        null
    }
}