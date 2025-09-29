package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.data.network.parsers.parseOrThrowError
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import okhttp3.ResponseBody
import retrofit2.Converter

private const val FORECAST_REFRESH_TIME = 1800000L

class ForecastWeatherRepositoryImpl(
    private val api: WeatherApi,
    private val appSettingsRepository: AppSettingsRepository,
    private val weatherDatabase: WeatherDatabase,
    private val errorConverter: Converter<ResponseBody, WeatherErrorResponse>,
) : ForecastWeatherRepository {

    private val settingsFlow = appSettingsRepository.settings
        .shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, 1)

    override fun observeBy(locationId: Int): Flow<ForecastWeatherDomainModel?> {
        return combine(
            weatherDatabase.forecastDao().observeForecastBy(locationId),
            settingsFlow
        ) { entity, settings ->
            if (entity == null || System.currentTimeMillis() - entity.location.lastUpdated > FORECAST_REFRESH_TIME) {
                null
            } else {
                entity.toDomain(settings)
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateBy(locationId: Int) {

        val location = weatherDatabase.matchingLocationsDao().getLocation(locationId)

        val response =
            api.getForecastWeather(query = location.toPoint(), lang = settingsFlow.first().language.value)
                .parseOrThrowError(errorConverter)
        weatherDatabase.forecastDao()
            .updateForecastForLocation(
                locationId = location.networkId,
                locationName = location.city,
                forecastResponse = response
            )
    }
}