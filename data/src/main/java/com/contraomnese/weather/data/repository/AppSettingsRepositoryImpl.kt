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

    override fun observeSettings(): Flow<AppSettings> =
        storage.observe().map { mapper.toDomain(it) }.flowOn(dispatcher)

    override suspend fun getSettings(): Result<AppSettings> = withContext(dispatcher) {
        try {
            val entity = storage.getSettings()
            val result = mapper.toDomain(entity)
            Result.success(result)
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is IllegalArgumentException -> operationFailed(logPrefix("Mapping failed: cannot convert AppSettings"), exception)
                    else -> storageError(logPrefix("Storage failure during saving Settings"), exception)
                }
            )
        }
    }

    override suspend fun updateSettings(settings: AppSettings): Result<Unit> = withContext(dispatcher) {
        try {
            val entity = mapper.toEntity(settings)
            storage.save(entity)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is IllegalArgumentException -> operationFailed(logPrefix("Mapping failed: cannot convert AppSettings"), exception)
                    else -> storageError(logPrefix("Storage failure during saving Settings"), exception)
                }
            )
        }
    }
}