package com.contraomnese.weather.domain.weatherByLocation.repository

import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {

    suspend fun updateForecastByLocationId(locationId: Int): Result<Int>
    fun observeSingleForecast(locationId: Int): Flow<Forecast?>
    fun observeForecasts(locationIds: List<Int>): Flow<List<Forecast>>
    fun observeFavoriteForecasts(locationIds: List<Int>): Flow<List<FavoriteForecast>>
    fun observeFavoriteForecast(locationId: Int): Flow<FavoriteForecast?>
}

