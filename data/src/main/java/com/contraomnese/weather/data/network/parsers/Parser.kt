package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.ErrorResponse
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
        throw Exception("HTTP ${code()}: $errorMessage")
    }
}