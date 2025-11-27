package com.contraomnese.weather.data.network.parsers

import com.contraomnese.weather.data.network.models.LocationsErrorResponse
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.domain.exceptions.apiUnavailable
import com.contraomnese.weather.domain.exceptions.badRequest
import com.contraomnese.weather.domain.exceptions.notFound
import com.contraomnese.weather.domain.exceptions.unauthorized
import com.contraomnese.weather.domain.exceptions.unknown
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

interface ApiParser {
    fun <T : Any> parseOrThrowError(response: Response<T>): T
}

class ApiParserImpl(
    private val converterFactory: Converter.Factory,
    private val retrofit: Retrofit,
) : ApiParser {

    override fun <T : Any> parseOrThrowError(response: Response<T>): T {
        if (response.isSuccessful) {
            val body = response.body()
                ?: throw IllegalStateException("Response body is null")
            return body
        }

        val errorBody = response.errorBody()

        when (response.raw().request.url.encodedPath) {

            "/forecast.json" -> {
                val weatherError = parseError<WeatherErrorResponse>(errorBody)
                throw buildWeatherException(weatherError, response.code())
            }

            "/search", "/reverse" -> {
                val locError = parseError<LocationsErrorResponse>(errorBody)
                throw buildLocationsException(locError, response.code())
            }
        }

        val msg = "Unknown API error"
        throw unknown("HTTP ${response.code()}: $msg")
    }

    private inline fun <reified T> parseError(errorBody: ResponseBody?): T? {
        if (errorBody == null) return null
        return try {
            val converter: Converter<ResponseBody, T> =
                retrofit.nextResponseBodyConverter(converterFactory, T::class.java, emptyArray())
            converter.convert(errorBody)
        } catch (e: Exception) {
            null
        }
    }

    private fun buildWeatherException(
        parsed: WeatherErrorResponse?,
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

    private fun buildLocationsException(
        parsed: LocationsErrorResponse?,
        code: Int,
    ): Exception {
        val message = parsed?.error ?: "Unknown location API error"
        val full = "HTTP $code: $message"

        return when (code) {
            1002, 2006, 2007, 2008, 2009 -> unauthorized(full)
            1003, 1005, 9000, 9001 -> badRequest(full)
            1006 -> notFound(full)
            9999 -> apiUnavailable(full)
            else -> unknown(full)
        }
    }
}