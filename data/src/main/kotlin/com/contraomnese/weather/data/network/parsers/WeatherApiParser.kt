package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.errors.INetworkError
import com.contraomnese.weather.data.network.models.errors.WeatherErrorResponse
import com.contraomnese.weather.domain.exceptions.apiUnavailable
import com.contraomnese.weather.domain.exceptions.badRequest
import com.contraomnese.weather.domain.exceptions.notFound
import com.contraomnese.weather.domain.exceptions.unauthorized
import com.contraomnese.weather.domain.exceptions.unknown
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

class WeatherApiParser(
    override val converterFactory: Converter.Factory,
    override val retrofit: Retrofit,
) : INetworkParser {

    override fun <T : Any> parseOrThrowError(response: Response<T>): T {
        if (response.isSuccessful) {
            val body = response.body()
                ?: throw IllegalStateException("Response body is null")
            return body
        }

        val errorBody = response.errorBody()

        val weatherError = parseError<WeatherErrorResponse>(errorBody)
        throw buildException(weatherError, response.code())

    }

    override fun <T : INetworkError> buildException(
        parsed: T?,
        code: Int,
    ): Exception {
        val error = parsed?.error
        val message = error?.message ?: "Unknown weather API error"
        val full = "HTTP $code: $message"

        return when (error?.code) {
            1002, 2006, 2007, 2008, 2009 -> unauthorized(full)
            1003, 1005, 9000, 9001 -> badRequest(full)
            1006 -> notFound(full)
            9999 -> apiUnavailable(full)
            else -> unknown(full)
        }
    }
}