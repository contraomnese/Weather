package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.forecast.openmeteo.takeForecastHourByTime
import com.contraomnese.weather.data.mappers.forecast.openmeteo.toEntity
import com.contraomnese.weather.data.mappers.forecast.openmeteo.toForecastAstroEntities
import com.contraomnese.weather.data.mappers.forecast.openmeteo.toForecastDailyEntities
import com.contraomnese.weather.data.mappers.forecast.openmeteo.toForecastDayEntities
import com.contraomnese.weather.data.mappers.forecast.openmeteo.toForecastHourlyEntities
import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toEntity
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toForecastDayEntity
import com.contraomnese.weather.data.network.remotes.weather.ForecastRemote
import com.contraomnese.weather.data.network.remotes.weather.OpenMeteoRemote
import com.contraomnese.weather.data.network.remotes.weather.WeatherApiRemote
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.exceptions.storageError
import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import java.util.concurrent.ConcurrentHashMap

class ForecastRepositoryImpl(
    private val forecastRemoteApi: ForecastRemote,
    private val appSettingsRepository: AppSettingsRepository,
    private val database: WeatherAppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val refreshForecastLocks: MutableMap<Int, Mutex> = ConcurrentHashMap(),
    private val transactionProvider: TransactionProvider,
) : ForecastRepository {

    override fun observeSingleForecast(locationId: Int): Flow<Forecast?> {
        return combine(
            database.forecastDao().observeForecastBy(locationId),
            appSettingsRepository.observeSettings()
        ) { entity, settings ->
            Pair(entity, settings)
        }
            .map { (entity, settings) ->
                entity?.let { entity.toDomain(settings) }
            }
            .flowOn(dispatcher)
    }

    override fun observeForecasts(locationIds: List<Int>): Flow<List<Forecast>> {
        return combine(
            database.forecastDao().observeForecastsBy(locationIds),
            appSettingsRepository.observeSettings()
        ) { entities, settings ->
            entities
                .filter { it.todayForecast != null }
                .map { it.toDomain(settings) }
        }
            .flowOn(dispatcher)
    }

    override fun observeFavoriteForecasts(locationIds: List<Int>): Flow<List<FavoriteForecast>> {
        return combine(
            database.forecastDao().observeFavoriteForecastsBy(locationIds),
            appSettingsRepository.observeSettings()
        ) { entities, settings ->
            entities
                .filter { it.todayForecast != null }
                .map { it.toDomain(settings) }
        }
            .flowOn(dispatcher)
    }

    override suspend fun updateForecastByLocationId(locationId: Int): Result<Int> {
        val mutex = refreshForecastLocks.computeIfAbsent(locationId) { Mutex() }

        return if (mutex.tryLock()) {
            try {
                updateForecast(locationId)
            } finally {
                mutex.unlock()
            }
        } else {
            Result.failure(operationFailed(logPrefix("Refreshing already launch for this location id")))
        }
    }

    private suspend fun updateForecast(locationId: Int): Result<Int> {
        return withContext(dispatcher) {

            val location = getForecastLocation(locationId).getOrElse {
                return@withContext Result.failure(it)
            }

            return@withContext when (forecastRemoteApi) {
                is OpenMeteoRemote -> updateForecastByOpenMeteoResponse(location, forecastRemoteApi)
                is WeatherApiRemote -> updateForecastByWeatherApiResponse(location, forecastRemoteApi)
            }.fold(
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

    private suspend fun getForecastLocation(locationId: Int): Result<ForecastLocationEntity> =
        try {
            Result.success(database.locationsDao().getForecastLocation(locationId))
        } catch (cause: Exception) {
            Result.failure(storageError(logPrefix("Current location didn't find in storage"), cause))
        }

    private suspend fun updateForecastByOpenMeteoResponse(
        location: ForecastLocationEntity,
        forecastRemote: OpenMeteoRemote,
    ) = try {
        val forecast = forecastRemote.fetchForecast(location)
        val airQuality = forecastRemote.fetchAirQuality(location)
        val forecastDaily = forecast.daily
        val forecastHourly = forecast.hourly

        transactionProvider.runWithTransaction {
            val locationsDao = database.locationsDao()
            locationsDao.deleteForecastLocation(location.locationId)

            val forecastLocationId = locationsDao.insertForecastLocation(location).toInt()

            val forecastDailyEntities = forecastDaily.toForecastDailyEntities(forecastLocationId)
            val forecastDayEntities = forecastDaily.toForecastDayEntities()
            val forecastAstroEntities = forecastDaily.toForecastAstroEntities(forecast.timezone)
            val forecastHourlyEntities = forecastHourly.toForecastHourlyEntities(airQuality)

            val forecastDao = database.forecastDao()
            forecastDao.insertForecastCurrent(
                forecast.current.toEntity(
                    forecastLocationId = forecastLocationId,
                    airQualityCurrent = airQuality,
                    forecastDayEntities,
                    forecastHourlyEntities
                )
            )

            forecastDailyEntities.forEachIndexed { index, forecastDaily ->
                val forecastDayId = forecastDao.insertForecastDay(forecastDaily).toInt()
                forecastDao.insertDay(forecastDayEntities[index].copy(forecastDailyId = forecastDayId))
                forecastDao.insertAstro(forecastAstroEntities[index].copy(forecastDailyId = forecastDayId))
                val insertForecastHour = forecastHourlyEntities.filter { it.takeForecastHourByTime(forecastDaily) }
                forecastDao.insertHourlyForecast(insertForecastHour.map { it.copy(forecastDailyId = forecastDayId) })
            }
        }
        Result.success(Unit)
    } catch (cause: Exception) {
        Result.failure(operationFailed(logPrefix("Impossible save new forecast to storage"), cause))
    }


    private suspend fun updateForecastByWeatherApiResponse(
        location: ForecastLocationEntity,
        forecastRemote: WeatherApiRemote,
    ) = try {

        val forecast = forecastRemote.fetchForecast(location)

        transactionProvider.runWithTransaction {
            val locationsDao = database.locationsDao()
            locationsDao.deleteForecastLocation(location.locationId)

            val forecastLocationId = locationsDao.insertForecastLocation(location).toInt()

            val forecastDao = database.forecastDao()
            forecastDao.insertForecastCurrent(current = forecast.current.toEntity(forecastLocationId))
            forecastDao.insertAlerts(forecast.alerts.alert.map { it.toEntity(forecastLocationId) })

            forecast.forecast.forecastDay.forEach { forecastDay ->
                val forecastDayId =
                    forecastDao.insertForecastDay(forecastDay.toForecastDayEntity(forecastLocationId)).toInt()
                forecastDao.insertDay(forecastDay.toEntity(forecastDayId))
                forecastDao.insertAstro(
                    forecastDay.astro.toEntity(
                        forecastDayId,
                        TimeZone.of(forecast.location.timeZoneId)
                    )
                )
                forecastDao.insertHourlyForecast(forecastDay.hour.map { it.toEntity(forecastDayId) })
            }
        }
        Result.success(Unit)
    } catch (cause: Exception) {
        Result.failure(operationFailed(logPrefix("Impossible save new forecast to storage"), cause))
    }
}
