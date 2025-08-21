package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel

interface ForecastWeatherRepository {

    suspend fun getBy(point: String): ForecastWeatherDomainModel

}

