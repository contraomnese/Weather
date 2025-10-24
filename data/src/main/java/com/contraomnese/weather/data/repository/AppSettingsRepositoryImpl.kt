package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.appSettings.toDomain
import com.contraomnese.weather.data.mappers.appSettings.toEntity
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.cleanarchitecture.exception.databaseError
import com.contraomnese.weather.domain.cleanarchitecture.exception.logPrefix
import com.contraomnese.weather.domain.cleanarchitecture.exception.operationFailed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AppSettingsRepositoryImpl(
    private val storage: AppSettingsStorage,
    private val dispatcher: CoroutineDispatcher,
) : AppSettingsRepository {

    override val settings: Flow<AppSettings> =
        storage.getSettings().map { it.toDomain() }.flowOn(dispatcher)

    override suspend fun updateSettings(settings: AppSettings): Result<Unit> {

        val storageSettings = try {
            settings.toEntity()
        } catch (throwable: Throwable) {
            return Result.failure(operationFailed(logPrefix("Impossible convert settings to database entity"), throwable))
        }

        return withContext(dispatcher) {
            try {
                storage.saveSettings(storageSettings)
                Result.success(Unit)
            } catch (throwable: Throwable) {
                Result.failure(databaseError(logPrefix("Impossible update settings in database"), throwable))
            }
        }
    }
}