package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.appSettings.toDomain
import com.contraomnese.weather.data.mappers.appSettings.toEntity
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class AppSettingsRepositoryImpl(
    private val storage: AppSettingsStorage,
) : AppSettingsRepository {

    override val settings: Flow<AppSettings> =
        storage.getSettings().map { it.toDomain() }.flowOn(Dispatchers.IO)

    override suspend fun updateSettings(settings: AppSettings) {
        storage.saveSettings(settings.toEntity())
    }
}