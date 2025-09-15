package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import kotlinx.coroutines.flow.Flow

interface ForecastWeatherRepository {

    suspend fun updateBy(location: DetailsLocationDomainModel)
    fun observeBy(location: DetailsLocationDomainModel): Flow<ForecastWeatherDomainModel?>
}

