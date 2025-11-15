package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.BiDirectMapper
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.AppSettings
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
    private val mapper: BiDirectMapper<AppSettingsEntity, AppSettings>,
    private val dispatcher: CoroutineDispatcher,
) : AppSettingsRepository {

    override fun observe(): Flow<AppSettings> =
        storage.getSettings().map { mapper.toDomain(it) }.flowOn(dispatcher)

    override suspend fun update(settings: AppSettings): Result<Unit> = withContext(dispatcher) {
        try {
            val entity = mapper.toEntity(settings)
            storage.saveSettings(entity)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is IllegalArgumentException -> operationFailed(logPrefix("Converting error"), exception)
                    else -> storageError(logPrefix("Storage saving error"), exception)
                }
            )
        }
    }
}