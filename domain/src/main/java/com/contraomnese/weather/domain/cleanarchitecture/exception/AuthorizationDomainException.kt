package com.contraomnese.weather.domain.cleanarchitecture.exception

class AuthorizationDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}