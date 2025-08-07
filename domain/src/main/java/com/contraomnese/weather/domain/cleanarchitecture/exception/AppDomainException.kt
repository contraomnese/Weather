package com.contraomnese.weather.domain.cleanarchitecture.exception

class AppDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}