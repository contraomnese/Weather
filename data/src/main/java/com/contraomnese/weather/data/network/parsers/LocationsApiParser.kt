package com.contraomnese.weather.data.network.parsers

import android.util.Log
import com.contraomnese.weather.data.network.models.LocationsErrorResponse
import com.contraomnese.weather.domain.cleanarchitecture.exception.apiUnavailable
import com.contraomnese.weather.domain.cleanarchitecture.exception.badRequest
import com.contraomnese.weather.domain.cleanarchitecture.exception.logPrefix
import com.contraomnese.weather.domain.cleanarchitecture.exception.notFound
import com.contraomnese.weather.domain.cleanarchitecture.exception.rateLimitExceeded
import com.contraomnese.weather.domain.cleanarchitecture.exception.unauthorized
import com.contraomnese.weather.domain.cleanarchitecture.exception.unknown
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
            400 -> throw badRequest(logPrefix(result))
            401 -> throw unauthorized(logPrefix(result))
            403 -> throw apiUnavailable(logPrefix(result))
            404 -> throw notFound(logPrefix(result))
            429 -> throw rateLimitExceeded(logPrefix(result))
            else -> throw unknown(logPrefix(result))
        }
    }
}
