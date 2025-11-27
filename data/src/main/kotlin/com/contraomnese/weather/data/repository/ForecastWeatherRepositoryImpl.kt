package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.UniDirectMapper
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.ForecastResponse
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastData
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.exceptions.badRequest
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.exceptions.storageError
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap

class ForecastWeatherRepositoryImpl(
    private val api: WeatherApi,
    private val apiParser: ApiParser,
    private val appSettingsRepository: AppSettingsRepository,
    private val weatherDatabase: WeatherDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val updateMutex: MutableMap<Int, Mutex> = ConcurrentHashMap(),
    private val mapper: UniDirectMapper<ForecastData, AppSettings, Forecast>,
) : ForecastWeatherRepository {

    override fun getForecastByLocationId(id: Int): Flow<Forecast?> {
        return combine(
            weatherDatabase.forecastDao().observeForecastBy(id),
            appSettingsRepository.observeSettings()
        ) { entity, settings ->
            Pair(entity, settings)
        }
            .map { (entity, settings) ->
                entity?.let { mapper.toDomain(it, settings) }
            }
            .flowOn(dispatcher)
    }

    override suspend fun refreshForecastByLocationId(id: Int): Result<Int> {
        val mutex = updateMutex.computeIfAbsent(id) { Mutex() }

        return if (mutex.tryLock()) {
            try {
                updateForecast(id)
            } finally {
                mutex.unlock()
            }
        } else {
            Result.failure(operationFailed(logPrefix("Refreshing already launch for this location id")))
        }
    }

    private suspend fun updateForecast(locationId: Int): Result<Int> {

        val location = getMatchingLocation(locationId).getOrElse {
            return Result.failure(it)
        }

        val forecast = getForecast(query = location.toPoint()).getOrElse {
            return Result.failure(it)
        }
        return updatingForecast(
            id = location.networkId,
            name = location.city ?: location.name,
            forecast = forecast
        ).fold(
            onSuccess = { Result.success(locationId) },
            onFailure = { Result.failure(it) }
        )
    }

    private suspend fun getMatchingLocation(locationId: Int): Result<MatchingLocationEntity> = withContext(dispatcher) {
        try {
            Result.success(weatherDatabase.matchingLocationsDao().getLocation(locationId))
        } catch (cause: Exception) {
            Result.failure(storageError(logPrefix("Current location didn't find in storage"), cause))
        }
    }

    private suspend fun getForecast(query: String) = withContext(dispatcher) {
        try {
            parseForecast(api.getForecastWeather(query = query))
        } catch (cause: Exception) {
            Result.failure(badRequest(logPrefix("Forecast not found"), cause))
        }
    }

    private fun parseForecast(forecast: Response<ForecastResponse>) =
        try {
            Result.success(apiParser.parseOrThrowError(forecast))
        } catch (cause: Exception) {
            Result.failure(cause)
        }


    private suspend fun updatingForecast(id: Int, name: String, forecast: ForecastResponse) = withContext(dispatcher) {
        try {
            Result.success(
                weatherDatabase.forecastDao().updateForecastForLocation(locationId = id, locationName = name, forecastResponse = forecast)
            )
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible save new forecast to storage"), cause))
        }
    }

}