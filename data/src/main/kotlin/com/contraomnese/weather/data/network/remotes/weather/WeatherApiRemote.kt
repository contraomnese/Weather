package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.network.responses.WeatherApiForecastResponse
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

class WeatherApiRemote(
    private val api: WeatherApi,
    private val parser: INetworkParser,
) : ForecastRemote {
    override suspend fun fetchForecast(location: MatchingLocationEntity): WeatherApiForecastResponse {
        val response = api.getForecast(location.toTextPoints())
        return parser.parseOrThrowError(response)
    }
}