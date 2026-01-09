package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.forecast.internal.toEntity
import com.contraomnese.weather.data.mappers.forecast.internal.toForecastDayEntity
import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.mappers.locations.toEntity
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.ForecastResponse
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.exceptions.badRequest
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.exceptions.storageError
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap

class ForecastRepositoryImpl(
    private val api: WeatherApi,
    private val apiParser: ApiParser,
    private val appSettingsRepository: AppSettingsRepository,
    private val database: WeatherAppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val refreshForecastLocks: MutableMap<Int, Mutex> = ConcurrentHashMap(),
    private val transactionProvider: TransactionProvider,
) : ForecastRepository {

    override fun observeForecastByLocationId(id: Int): Flow<Forecast?> {
        return combine(
            database.forecastDao().observeForecastBy(id),
            appSettingsRepository.observeSettings()
        ) { entity, settings ->
            Pair(entity, settings)
        }
            .map { (entity, settings) ->
                entity?.let { entity.toDomain(settings) }
            }
            .flowOn(dispatcher)
    }

    override fun observeForecastsByLocationIds(ids: List<Int>): Flow<List<Forecast>> {
        return combine(
            database.forecastDao().observeForecastsBy(ids),
            appSettingsRepository.observeSettings()
        ) { entities, settings ->
            entities.map { it.toDomain(settings) }
        }
            .flowOn(dispatcher)
    }

    override suspend fun refreshForecastByLocationId(id: Int): Result<Int> {
        val mutex = refreshForecastLocks.computeIfAbsent(id) { Mutex() }

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
        return withContext(dispatcher) {
            val location = getMatchingLocation(locationId).getOrElse {
                return@withContext Result.failure(it)
            }

            val forecast = getForecast(query = location.toPoint()).getOrElse {
                return@withContext Result.failure(it)
            }
            return@withContext updatingForecast(
                locationId = location.networkId,
                locationName = location.city ?: location.name,
                forecastResponse = forecast
            ).fold(
                onSuccess = { Result.success(locationId) },
                onFailure = { Result.failure(it) }
            )
        }
    }

    private suspend fun getMatchingLocation(locationId: Int): Result<MatchingLocationEntity> =
        try {
            Result.success(database.locationsDao().getMatchingLocation(locationId))
        } catch (cause: Exception) {
            Result.failure(storageError(logPrefix("Current location didn't find in storage"), cause))
        }


    private suspend fun getForecast(query: String) =
        try {
            parseForecast(api.getForecastWeather(query = query))
        } catch (cause: Exception) {
            Result.failure(badRequest(logPrefix("Forecast not found"), cause))
        }


    private fun parseForecast(forecast: Response<ForecastResponse>) =
        try {
            Result.success(apiParser.parseOrThrowError(forecast))
        } catch (cause: Exception) {
            Result.failure(cause)
        }


    private suspend fun updatingForecast(locationId: Int, locationName: String, forecastResponse: ForecastResponse) =
        try {
            transactionProvider.runWithTransaction {
                val locationsDao = database.locationsDao()
                locationsDao.deleteForecastLocation(locationId)

                val locationEntity = forecastResponse.location.toEntity(locationId).copy(name = locationName)
                val forecastLocationId = locationsDao.insertForecastLocation(locationEntity).toInt()

                val forecastDao = database.forecastDao()
                forecastDao.insertForecastCurrent(forecastResponse.current.toEntity(forecastLocationId))
                forecastDao.insertAlerts(forecastResponse.alerts.alert.map { it.toEntity(forecastLocationId) })

                forecastResponse.forecast.forecastDay.forEach { forecast ->
                    val forecastDayId = forecastDao.insertForecastDay(forecast.toForecastDayEntity(forecastLocationId)).toInt()
                    forecastDao.insertDay(forecast.toEntity(forecastDayId))
                    forecastDao.insertAstro(forecast.astro.toEntity(forecastDayId))
                    forecastDao.insertHourlyForecast(forecast.hour.map { it.toEntity(forecastDayId) })
                }
            }
            Result.success(Unit)
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible save new forecast to storage"), cause))
        }


}