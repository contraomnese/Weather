package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.CurrentWeatherDomainModel

interface CurrentWeatherRepository {

    suspend fun getWeatherBy(point: String): CurrentWeatherDomainModel

}

