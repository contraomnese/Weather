package com.contraomnese.weather.domain

import org.junit.jupiter.api.Assertions.assertTrue

fun <T> Result<T>.assertIsSuccess(): T {
    assertTrue(this.isSuccess, "Expected Result to be Success, but was Failure: ${this.exceptionOrNull()?.message}")
    return this.getOrThrow()
}

fun <T> Result<T>.assertIsFailure(): Throwable {
    assertTrue(this.isFailure, "Expected Result to be Failure, but was Success: ${this.getOrElse { null }}")
    return this.exceptionOrNull() ?: throw AssertionError("Expected exception, but none found.")
}