package com.contraomnese.weather.domain.cleanarchitecture.exception

class WeatherApiUnavailableDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}