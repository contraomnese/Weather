package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity

sealed interface ForecastRemote {
    suspend fun fetchForecast(location: ForecastLocationEntity): Any
}