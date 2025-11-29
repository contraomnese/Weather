package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {

    suspend fun refreshForecastByLocationId(id: Int): Result<Int>
    fun getForecastByLocationId(id: Int): Flow<Forecast?>
}

