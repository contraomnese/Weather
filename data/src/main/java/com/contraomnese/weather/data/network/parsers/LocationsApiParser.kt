package com.contraomnese.weather.data.network.parsers

import android.util.Log
import com.contraomnese.weather.data.network.models.LocationsErrorResponse
import com.contraomnese.weather.domain.cleanarchitecture.exception.ApiUnavailableDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.AuthorizationDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.LocationNotFoundDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.RateLimitedDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.RequestDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response

fun <T> Response<T>.parseOrThrowError(converter: Converter<ResponseBody, LocationsErrorResponse>): T {
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
        val errorMessage = parsedError?.error ?: "Unknown error"
        val result = "HTTP ${code()}: $errorMessage"
        Log.d("LocationsParser", result)
        when (this.code()) {
            400 -> throw RequestDomainException(result)
            401 -> throw AuthorizationDomainException(result)
            403 -> throw ApiUnavailableDomainException(result)
            404 -> throw LocationNotFoundDomainException(result)
            429 -> throw RateLimitedDomainException(result)
            else -> throw UnknownDomainException(result)
        }
    }
}
