package com.contraomnese.weather.domain.cleanarchitecture.exception

class ApiUnavailableDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}