package com.contraomnese.weather.domain.locationForecast.repository

import com.contraomnese.weather.domain.locationForecast.model.CurrentWeatherDomainModel

interface CurrentWeatherRepository {

    suspend fun getWeatherBy(point: String): CurrentWeatherDomainModel

}

