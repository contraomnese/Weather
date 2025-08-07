package com.contraomnese.weather.data.network.parsers

import android.util.Log
import com.contraomnese.weather.data.network.models.ErrorResponse
import com.contraomnese.weather.domain.cleanarchitecture.exception.AuthorizationDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.LocationNotFoundDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.RequestDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.WeatherApiUnavailableDomainException
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response

suspend fun <T> Response<T>.parseOrThrowError(converter: Converter<ResponseBody, ErrorResponse>): T {
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
            1002, 2006, 2007, 2008, 2009 -> throw AuthorizationDomainException(result)
            1003, 1005, 9000, 9001 -> throw RequestDomainException(result)
            1006 -> throw LocationNotFoundDomainException(result)
            9999 -> throw WeatherApiUnavailableDomainException(result)
            else -> throw UnknownDomainException(result)
        }
    }
}