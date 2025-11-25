package com.contraomnese.weather.domain


import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

object ForecastDomainFixtures {

    fun generateRandom() = generateFake(Forecast::class) as Forecast

    fun generateReal() = MockForecastReal.take()

}