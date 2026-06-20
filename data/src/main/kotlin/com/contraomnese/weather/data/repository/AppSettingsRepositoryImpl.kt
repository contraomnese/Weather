package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.appSettings.AppSettingsMapper
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.exceptions.storageError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AppSettingsRepositoryImpl(
    private val storage: AppSettingsStorage,
    private val mapper: AppSettingsMapper = AppSettingsMapper(),
    private val dispatcher: CoroutineDispatcher,
) : AppSettingsRepository {

    override fun observeSettings(): Flow<AppSettings> =
        storage.observeSettings().map { mapper.toDomain(it) }.flowOn(dispatcher)

    override fun getSettings(): Result<AppSettings> =
        try {
            val entity = storage.getSettings()
            val result = mapper.toDomain(entity)
            Result.success(result)
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is IllegalArgumentException -> operationFailed(
                        logPrefix("Mapping failed: cannot convert AppSettings"),
                        exception
                    )

                    else -> storageError(logPrefix("Storage failure during saving Settings"), exception)
                }
            )
        }


    override suspend fun updateSettings(settings: AppSettings): Result<Unit> = withContext(dispatcher) {
        try {
            val entity = mapper.toEntity(settings)
            storage.saveSettings(entity)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is IllegalArgumentException -> operationFailed(
                        logPrefix("Mapping failed: cannot convert AppSettings"),
                        exception
                    )

                    else -> storageError(logPrefix("Storage failure during saving Settings"), exception)
                }
            )
        }
    }

    override suspend fun setLanguage(language: Language): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setLanguage(language)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(storageError(logPrefix("Storage failure during change language"), exception))
        }
    }


    override suspend fun setTimezone(timezone: String): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setTimezone(timezone)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(storageError(logPrefix("Storage failure during change timezone"), exception))
        }
    }

    override suspend fun setWindSpeedUnit(unit: WindSpeedUnit): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setWindSpeedUnit(unit)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(storageError(logPrefix("Storage failure during change wind speed unit"), exception))
        }
    }

    override suspend fun setPrecipitationUnit(unit: PrecipitationUnit): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setPrecipitationUnit(unit)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(storageError(logPrefix("Storage failure during change precipitation unit"), exception))
        }
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setTemperatureUnit(unit)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(storageError(logPrefix("Storage failure during change temperature unit"), exception))
        }
    }

    override suspend fun setPressureUnit(unit: PressureUnit): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setPressureUnit(unit)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(storageError(logPrefix("Storage failure during change pressure unit"), exception))
        }
    }

    override suspend fun setForecastAutoSyncEnabled(enabled: Boolean): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setForecastAutoSyncEnabled(enabled)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                storageError(
                    logPrefix("Storage failure during enable/disable auto sync forecast"),
                    exception
                )
            )
        }
    }

    override suspend fun setPushNotificationEnabled(enabled: Boolean): Result<Unit> = withContext(dispatcher) {
        try {
            storage.setNotificationEnabled(enabled)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                storageError(
                    logPrefix("Storage failure during enable/disable notifications"),
                    exception
                )
            )
        }
    }
}