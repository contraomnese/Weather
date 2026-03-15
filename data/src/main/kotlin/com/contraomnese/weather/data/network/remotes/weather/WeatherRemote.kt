package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.network.responses.WeatherApiForecastResponse

interface WeatherRemote {
    suspend fun fetchForecast(query: String): Result<WeatherApiForecastResponse>
}