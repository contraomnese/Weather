package com.contraomnese.weather.data.network.parsers

import android.util.Log
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.domain.exceptions.apiUnavailable
import com.contraomnese.weather.domain.exceptions.badRequest
import com.contraomnese.weather.domain.exceptions.notFound
import com.contraomnese.weather.domain.exceptions.unauthorized
import com.contraomnese.weather.domain.exceptions.unknown
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response

fun <T> Response<T>.parseOrThrowError(converter: Converter<ResponseBody, WeatherErrorResponse>): T {
    if (isSuccessful) {
        val body = body()
        if (body != null) return body
        throw Exception("Response body is null")
    } else {
        val errorBody = errorBody()
        val parsedError = errorBody?.let {
            try {
                converter.convert(it)
            } catch (e: Exception) {
                null
            }
        }
        val errorMessage = parsedError?.error?.message ?: "Unknown error"
        val result = "HTTP ${code()}: $errorMessage"
        Log.d("WeatherParser", result)
        when (parsedError?.error?.code) {
            1002, 2006, 2007, 2008, 2009 -> throw unauthorized(result)
            1003, 1005, 9000, 9001 -> throw badRequest(result)
            1006 -> throw notFound(result)
            9999 -> throw apiUnavailable(result)
            else -> throw unknown(result)
        }
    }
}