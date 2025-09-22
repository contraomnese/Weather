package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import retrofit2.Response

fun <T> Response<T>.parseOrThrowError(): T {
    if (isSuccessful) {
        val body = body()
        if (body != null) return body
        throw Exception("Response body is null")
    } else {
        throw UnknownDomainException("${code()} ${message()}")
    }
}
