package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.network.api.OpenMeteoAirQualityApi
import com.contraomnese.weather.data.network.api.OpenMeteoForecastApi
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.network.responses.OpenMeteoAirQualityResponse
import com.contraomnese.weather.data.network.responses.OpenMeteoForecastResponse
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

class OpenMeteoRemote(
    private val forecastApi: OpenMeteoForecastApi,
    private val airQualityApi: OpenMeteoAirQualityApi,
    private val parser: INetworkParser,
) : ForecastRemote {
    override suspend fun fetchForecast(
        location: MatchingLocationEntity,
    ): OpenMeteoForecastResponse {
        val forecastResponse = forecastApi.getForecast(
            latitude = location.latitude,
            longitude = location.longitude,
            timezone = location.timeZoneId ?: "GMT"
        )

        return parser.parseOrThrowError(forecastResponse)
    }

    suspend fun fetchAirQuality(location: MatchingLocationEntity): OpenMeteoAirQualityResponse {
        val airQualityResponse = airQualityApi.getAirQuality(
            latitude = location.latitude,
            longitude = location.longitude,
            timezone = location.timeZoneId ?: "GMT"
        )

        return parser.parseOrThrowError(airQualityResponse)
    }
}