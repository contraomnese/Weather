package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

sealed interface ForecastRemote {
    suspend fun fetchForecast(location: MatchingLocationEntity): Any
}