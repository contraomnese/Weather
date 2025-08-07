package com.contraomnese.weather.domain.cleanarchitecture.exception

class RequestDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}