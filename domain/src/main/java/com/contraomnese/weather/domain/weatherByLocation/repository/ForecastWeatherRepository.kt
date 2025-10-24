package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastWeatherRepository {

    suspend fun updateBy(locationId: Int): Result<Unit>
    fun observeBy(locationId: Int): Flow<Forecast?>
}

