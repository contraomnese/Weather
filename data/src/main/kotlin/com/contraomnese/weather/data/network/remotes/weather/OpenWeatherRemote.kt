package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.network.api.OpenWeatherAirQualityApi
import com.contraomnese.weather.data.network.api.OpenWeatherForecastApi
import com.contraomnese.weather.data.network.models.openweather.airquality.AirQualityNetwork
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.network.responses.OpenWeatherForecastResponse
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

class OpenWeatherRemote(
    private val forecastApi: OpenWeatherForecastApi,
    private val airQualityApi: OpenWeatherAirQualityApi,
    private val parser: INetworkParser,
) : ForecastRemote {
    override suspend fun fetchForecast(
        location: MatchingLocationEntity,
    ): OpenWeatherForecastResponse {
        val forecastResponse = forecastApi.getForecast(
            latitude = location.latitude,
            longitude = location.longitude,
            timezone = location.timeZoneId ?: "GMT"
        )

        return parser.parseOrThrowError(forecastResponse)
    }

    suspend fun fetchAirQuality(location: MatchingLocationEntity): AirQualityNetwork {
        val airQualityResponse = airQualityApi.getAirQuality(
            latitude = location.latitude,
            longitude = location.longitude,
            timezone = location.timeZoneId ?: "GMT"
        )

        return parser.parseOrThrowError(airQualityResponse)
    }
}