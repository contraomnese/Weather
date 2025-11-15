package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.data.network.parsers.parseOrThrowError
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.exceptions.storageError
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter

private const val FORECAST_REFRESH_TIME = 1800000L

class ForecastWeatherRepositoryImpl(
    private val api: WeatherApi,
    private val appSettingsRepository: AppSettingsRepository,
    private val weatherDatabase: WeatherDatabase,
    private val errorConverter: Converter<ResponseBody, WeatherErrorResponse>,
    private val dispatcher: CoroutineDispatcher,
) : ForecastWeatherRepository {

    private val settingsFlow = appSettingsRepository.observe()
        .shareIn(CoroutineScope(dispatcher), SharingStarted.Eagerly, 1)

    override fun observeBy(locationId: Int): Flow<Forecast?> {
        return combine(
            weatherDatabase.forecastDao().observeForecastBy(locationId),
            settingsFlow
        ) { entity, settings ->
            if (entity == null || System.currentTimeMillis() - entity.location.lastUpdated > FORECAST_REFRESH_TIME) {
                null
            } else {
                entity.toDomain(settings)
            }
        }.flowOn(dispatcher)
    }

    override suspend fun updateBy(locationId: Int): Result<Unit> {

        val location = try {
            withContext(dispatcher) {
                weatherDatabase.matchingLocationsDao().getLocation(locationId)
            }
        } catch (throwable: Throwable) {
            return Result.failure(storageError(logPrefix("Get location from database failed"), throwable))
        }

        val response = try {
            withContext(dispatcher) {
                api.getForecastWeather(query = location.toPoint(), lang = settingsFlow.first().language.value)
                    .parseOrThrowError(errorConverter)
            }
        } catch (cause: Throwable) {
            return Result.failure(cause)
        }

        return try {
            withContext(dispatcher) {
                weatherDatabase.forecastDao()
                    .updateForecastForLocation(
                        locationId = location.networkId,
                        locationName = location.city ?: location.name,
                        forecastResponse = response
                    )
            }
            Result.success(Unit)
        } catch (throwable: Throwable) {
            Result.failure(operationFailed(logPrefix("Impossible save new forecast in database"), throwable))
        }
    }
}