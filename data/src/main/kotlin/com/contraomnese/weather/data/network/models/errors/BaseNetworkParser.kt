package com.contraomnese.weather.data.network.models.errors

import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.domain.exceptions.DomainException
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseNetworkParser<E : INetworkError>(
    private val errorClass: Class<E>,
) : INetworkParser {

    protected abstract val unauthorizedCodes: Set<Int>
    protected abstract val badRequestCodes: Set<Int>
    protected abstract val notFoundCodes: Set<Int>
    protected abstract val unavailableCodes: Set<Int>
    protected abstract val rateLimitCodes: Set<Int>

    protected abstract fun provideApiCode(parsed: E?): Int?
    protected abstract fun provideMessage(parsed: E?): String?

    protected fun unauthorized(msg: String) = DomainException.Unauthorized("Unauthorized: $msg")
    protected fun badRequest(msg: String) = DomainException.BadRequest("Bad Request: $msg")
    protected fun notFound(msg: String) = DomainException.NotFound("Not Found: $msg")
    protected fun apiUnavailable(msg: String) = DomainException.ApiUnavailable("Service Unavailable: $msg")
    protected fun unknown(msg: String) = DomainException.Unknown("Unknown Error: $msg")
    protected fun rateLimitExceeded(msg: String) = DomainException.RateLimitExceeded("Rate limits are done: $msg")

    private fun mapCodeToCategory(code: Int?): NetworkErrorCategory {
        return when (code) {
            in unauthorizedCodes -> NetworkErrorCategory.Unauthorized
            in badRequestCodes -> NetworkErrorCategory.BadRequest
            in notFoundCodes -> NetworkErrorCategory.NotFound
            in unavailableCodes -> NetworkErrorCategory.Unavailable
            in rateLimitCodes -> NetworkErrorCategory.RateLimits
            else -> NetworkErrorCategory.Unknown
        }
    }

    override fun <T : Any> parseOrThrowError(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("Empty body")
        }

        val errorDto = parseError(response.errorBody())
        throw buildException(errorDto, response.code())
    }

    override fun <T : INetworkError> buildException(parsed: T?, httpCode: Int): Exception {
        val errorDto = parsed as? E

        val apiCode = provideApiCode(errorDto)
        val message = provideMessage(errorDto) ?: "Unknown Error"
        val fullMessage = "HTTP $httpCode: $message"

        val category = mapCodeToCategory(apiCode ?: httpCode)

        return when (category) {
            NetworkErrorCategory.Unauthorized -> unauthorized(fullMessage)
            NetworkErrorCategory.BadRequest -> badRequest(fullMessage)
            NetworkErrorCategory.NotFound -> notFound(fullMessage)
            NetworkErrorCategory.Unavailable -> apiUnavailable(fullMessage)
            NetworkErrorCategory.RateLimits -> rateLimitExceeded(fullMessage)
            NetworkErrorCategory.Unknown -> unknown(fullMessage)
        }
    }

    private fun parseError(errorBody: ResponseBody?): E? {
        if (errorBody == null) return null
        return try {
            val converter = retrofit.responseBodyConverter<E>(errorClass, emptyArray())
            converter.convert(errorBody)
        } catch (e: Exception) {
            null
        }
    }
}