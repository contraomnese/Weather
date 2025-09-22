package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import kotlinx.coroutines.flow.Flow

interface ForecastWeatherRepository {

    suspend fun updateBy(location: LocationInfoDomainModel)
    fun observeBy(location: LocationInfoDomainModel): Flow<ForecastWeatherDomainModel?>
}

