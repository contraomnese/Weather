package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import kotlinx.coroutines.flow.Flow

interface ForecastWeatherRepository {

    suspend fun updateBy(locationId: Int)
    fun observeBy(locationId: Int): Flow<ForecastWeatherDomainModel?>
}

