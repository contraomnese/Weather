package com.contraomnese.weather.presentation.utils

inline fun <reified R> Result<R>.takeIfSuccess(): R? =
    if (this.isSuccess) this.getOrNull() else null

inline fun <reified R> Result<R>.handle(
    onSuccess: (R) -> Unit,
    onError: (Throwable) -> Unit,
): Unit = if (this.isSuccess) {
    try {
        val value = getOrThrow()
        onSuccess(value)
    } catch (e: Exception) {
        onError(e)
    }
} else {
    onError(this.exceptionOrNull() ?: Throwable())
}

inline fun <reified R, reified T> Result<R>.handleMap(
    onSuccess: (R) -> T,
    onError: (Throwable) -> T,
): T = if (this.isSuccess) {
    try {
        val value = getOrThrow()
        onSuccess(value)
    } catch (e: Exception) {
        onError(e)
    }
} else {
    onError(this.exceptionOrNull() ?: Throwable())
}

inline fun <reified R, reified T> Result<R>.map(mapper: (R) -> T): Result<T> =
    if (this.isSuccess) {
        try {
            val value = getOrThrow()
            Result.success(mapper(value))
        } catch (e: Exception) {
            Result.failure(e)
        }
    } else {
        Result.failure(this.exceptionOrNull() ?: Throwable())
    }