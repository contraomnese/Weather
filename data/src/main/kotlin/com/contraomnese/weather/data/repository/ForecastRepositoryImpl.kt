package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.forecast.internal.toEntity
import com.contraomnese.weather.data.mappers.forecast.internal.toForecastDayEntity
import com.contraomnese.weather.data.mappers.forecast.openmeteo.toEntity
import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toEntity
import com.contraomnese.weather.data.mappers.locations.toForecastLocationEntity
import com.contraomnese.weather.data.mappers.utils.celsiusToFahrenheit
import com.contraomnese.weather.data.mappers.utils.hPaToInchesHg
import com.contraomnese.weather.data.mappers.utils.kmToMiles
import com.contraomnese.weather.data.mappers.utils.kphToMph
import com.contraomnese.weather.data.mappers.utils.mmToInch
import com.contraomnese.weather.data.network.remotes.weather.ForecastRemote
import com.contraomnese.weather.data.network.remotes.weather.OpenWeatherRemote
import com.contraomnese.weather.data.network.remotes.weather.WeatherApiRemote
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.data.utils.getAmPmTime
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
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
import java.util.concurrent.ConcurrentHashMap

class ForecastRepositoryImpl(
    private val forecastRemoteApi: ForecastRemote,
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

            return@withContext when (forecastRemoteApi) {
                is OpenWeatherRemote -> updateForecastByOpenWeatherResponse(location, forecastRemoteApi)
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

    private suspend fun updateForecastByOpenWeatherResponse(
        location: MatchingLocationEntity,
        forecastRemote: OpenWeatherRemote,
    ) = try {
        val forecast = forecastRemote.fetchForecast(location)
        val airQuality = forecastRemote.fetchAirQuality(location)
        val forecastDaily = forecast.daily
        val forecastHourly = forecast.hourly

        transactionProvider.runWithTransaction {
            val locationsDao = database.locationsDao()
            locationsDao.deleteForecastLocation(location.networkId)

            val locationEntity = location.toForecastLocationEntity(location.networkId)
            val forecastLocationId = locationsDao.insertForecastLocation(locationEntity).toInt()

            val forecastDailyEntities = forecastDaily.time.indices.map { index ->
                ForecastDailyEntity(
                    forecastLocationId = forecastLocationId,
                    dateEpoch = forecast.daily.time[index],
                )
            }

            val forecastDayEntities = forecastDaily.time.indices.map { index ->
                ForecastDayEntity(
                    forecastDailyId = index,
                    maxTempC = forecastDaily.temperature2mMax[index],
                    maxTempF = celsiusToFahrenheit(forecastDaily.temperature2mMax[index]),
                    minTempC = forecastDaily.temperature2mMin[index],
                    minTempF = celsiusToFahrenheit(forecastDaily.temperature2mMin[index]),
                    avgTempC = forecastDaily.temperature2mMean[index],
                    avgTempF = celsiusToFahrenheit(forecastDaily.temperature2mMean[index]),
                    maxWindKph = forecastDaily.windSpeed10mMax[index],
                    maxWindMph = kphToMph(forecastDaily.windSpeed10mMax[index]),
                    totalPrecipMm = forecastDaily.precipitationSum[index],
                    totalPrecipIn = mmToInch(forecastDaily.precipitationSum[index]),
                    totalSnowCm = forecastDaily.snowfallSum[index],
                    avgVisKm = forecastDaily.visibilityMean[index],
                    avgVisMiles = kmToMiles(forecastDaily.visibilityMean[index]),
                    avgHumidity = forecastDaily.relativeHumidity2mMean[index].toInt(),
                    conditionCode = forecastDaily.weatherCode[index],
                    uv = forecastDaily.uvIndexMax[index],
                    // TODO this is not a chance
                    dayWillItRain = forecastDaily.precipitationProbabilityMax[index],
                    dayWillItSnow = forecastDaily.precipitationProbabilityMax[index],
                    dayChanceOfRain = forecastDaily.precipitationProbabilityMax[index],
                    dayChanceOfSnow = forecastDaily.precipitationProbabilityMax[index]
                )
            }

            val forecastAstroEntities = forecastDaily.time.indices.map { index ->
                ForecastAstroEntity(
                    forecastDailyId = index,
                    sunrise = getAmPmTime(forecastDaily.sunrise[index], forecast.timezone),
                    sunset = getAmPmTime(forecastDaily.sunset[index], forecast.timezone),
                    isSunUp = if (forecastDaily.time[index] > forecastDaily.sunrise[index]) 1 else 0
                )
            }

            val forecastHourlyEntities = forecastHourly.time.indices.map { index ->
                ForecastHourEntity(
                    forecastDailyId = index,
                    timeEpoch = forecastHourly.time[index],
                    tempC = forecastHourly.temperature2m[index],
                    tempF = celsiusToFahrenheit(forecastHourly.temperature2m[index]),
                    isDay = forecastHourly.isDay[index],
                    conditionCode = forecastHourly.weatherCode[index],
                    windKph = forecastHourly.windSpeed10m[index],
                    windMph = kphToMph(forecastHourly.windSpeed10m[index]),
                    windDegree = forecastHourly.windDirection10m[index],
                    pressureMb = forecastHourly.surfacePressure[index],
                    pressureIn = hPaToInchesHg(forecastHourly.surfacePressure[index]),
                    precipMm = forecastHourly.precipitation[index],
                    precipIn = mmToInch(forecastHourly.precipitation[index]),
                    snowCm = forecastHourly.snowfall[index],
                    humidity = forecastHourly.relativeHumidity2m[index],
                    cloud = forecastHourly.cloudCover[index],
                    feelsLikeC = forecastHourly.apparentTemperature[index],
                    feelsLikeF = celsiusToFahrenheit(forecastHourly.apparentTemperature[index]),
                    dewPointC = forecastHourly.dewPoint2m[index],
                    dewPointF = celsiusToFahrenheit(forecastHourly.dewPoint2m[index]),
                    chanceOfRain = forecastHourly.precipitationProbability[index],
                    chanceOfSnow = forecastHourly.precipitationProbability[index],
                    visibilityKm = forecastHourly.visibility[index],
                    visibilityMiles = kmToMiles(forecastHourly.visibility[index]),
                    gustKph = forecastHourly.windGusts10m[index],
                    gustMph = kphToMph(forecastHourly.windGusts10m[index]),
                    uv = airQuality.hourly.uvIndex[index]
                )
            }

            val forecastDao = database.forecastDao()
            forecastDao.insertForecastCurrent(
                forecast.current.toEntity(
                    forecastLocationId = forecastLocationId,
                    visibility = forecastDayEntities[0].avgVisKm,
                    dewPoint = forecastHourlyEntities.filter { it.timeEpoch < forecast.current.time + 86400 }
                        .sumOf { it.dewPointC } / 24,
                    airQualityCurrent = airQuality,
                    updatedTime = forecast.current.time
                )
            )

            forecastDailyEntities.forEachIndexed { index, forecastDaily ->
                val forecastDayId = forecastDao.insertForecastDay(forecastDaily).toInt()
                forecastDao.insertDay(forecastDayEntities[index].copy(forecastDailyId = forecastDayId))
                forecastDao.insertAstro(forecastAstroEntities[index].copy(forecastDailyId = forecastDayId))
                val insertForecastHour =
                    forecastHourlyEntities.filter { it.timeEpoch < forecastDaily.dateEpoch + 86400 && it.timeEpoch >= forecastDaily.dateEpoch } // 86400 seconds in a day
                forecastDao.insertHourlyForecast(insertForecastHour.map { it.copy(forecastDailyId = forecastDayId) })
            }
        }
        Result.success(Unit)
    } catch (cause: Exception) {
        Result.failure(operationFailed(logPrefix("Impossible save new forecast to storage"), cause))
    }


    private suspend fun updateForecastByWeatherApiResponse(
        location: MatchingLocationEntity,
        forecastRemote: WeatherApiRemote,
    ) = try {

        val forecast = forecastRemote.fetchForecast(location)

        transactionProvider.runWithTransaction {
            val locationsDao = database.locationsDao()
            locationsDao.deleteForecastLocation(location.networkId)

            val locationEntity = location.toForecastLocationEntity(location.networkId)
            val forecastLocationId = locationsDao.insertForecastLocation(locationEntity).toInt()

            val forecastDao = database.forecastDao()
            forecastDao.insertForecastCurrent(forecast.current.toEntity(forecastLocationId))
            forecastDao.insertAlerts(forecast.alerts.alert.map { it.toEntity(forecastLocationId) })

            forecast.forecast.forecastDay.forEach { forecast ->
                val forecastDayId =
                    forecastDao.insertForecastDay(forecast.toForecastDayEntity(forecastLocationId)).toInt()
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
