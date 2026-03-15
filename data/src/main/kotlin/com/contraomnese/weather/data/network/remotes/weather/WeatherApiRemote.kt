package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.network.responses.WeatherApiForecastResponse

class WeatherApiRemote(
    private val api: WeatherApi,
    private val parser: INetworkParser,
) : WeatherRemote {
    override suspend fun fetchForecast(query: String): Result<WeatherApiForecastResponse> {
        val response = api.getForecast(query)
        return try {
            Result.success(parser.parseOrThrowError(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}