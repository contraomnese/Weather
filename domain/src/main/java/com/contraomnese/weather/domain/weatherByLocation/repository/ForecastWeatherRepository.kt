package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.WeatherDomainModel

interface ForecastWeatherRepository {

    suspend fun getBy(point: String): WeatherDomainModel

}

