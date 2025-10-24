package com.contraomnese.weather.domain.cleanarchitecture.exception

sealed class DomainException(
    message: String?,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class ApiUnavailable(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class Unauthorized(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class RateLimitExceeded(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class NotFound(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class BadRequest(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class OperationFailed(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class DatabaseError(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class InitializeError(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
    data class Unknown(val msg: String?, val err: Throwable? = null) : DomainException(msg, err)
}

fun apiUnavailable(message: String, cause: Throwable? = null) =
    DomainException.ApiUnavailable(message, cause)

fun unauthorized(message: String, cause: Throwable? = null) =
    DomainException.Unauthorized(message, cause)

fun rateLimitExceeded(message: String, cause: Throwable? = null) =
    DomainException.RateLimitExceeded(message, cause)

fun notFound(message: String, cause: Throwable? = null) =
    DomainException.NotFound(message, cause)

fun badRequest(message: String, cause: Throwable? = null) =
    DomainException.BadRequest(message, cause)

fun operationFailed(message: String, cause: Throwable? = null) =
    DomainException.OperationFailed(message, cause)

fun databaseError(message: String, cause: Throwable? = null) =
    DomainException.DatabaseError(message, cause)

fun unknown(message: String, cause: Throwable? = null) =
    DomainException.Unknown(message, cause)

fun notInitialize(message: String, cause: Throwable? = null) =
    DomainException.InitializeError(message, cause)

fun Any.logPrefix(message: String): String =
    "[${this.javaClass.simpleName}] $message"