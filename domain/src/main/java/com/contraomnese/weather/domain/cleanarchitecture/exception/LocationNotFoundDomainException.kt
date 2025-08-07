package com.contraomnese.weather.domain.cleanarchitecture.exception

class LocationNotFoundDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}