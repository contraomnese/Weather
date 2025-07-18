package com.contraomnese.weather.domain.cleanarchitecture.exception

abstract class DomainException(throwable: Throwable) : Exception(throwable) {
    constructor(message: String) : this(Exception(message))
    constructor(message: String, throwable: Throwable) : this(Exception(message, throwable))
}