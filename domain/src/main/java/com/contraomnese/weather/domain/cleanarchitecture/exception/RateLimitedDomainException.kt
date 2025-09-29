package com.contraomnese.weather.domain.cleanarchitecture.exception

class RateLimitedDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}