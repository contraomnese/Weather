package com.contraomnese.weather.data.storage.memory.api

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import kotlinx.coroutines.flow.Flow

interface AppSettingsStorage {
    fun observe(): Flow<AppSettingsEntity>
    suspend fun getSettings(): AppSettingsEntity
    suspend fun save(entity: AppSettingsEntity)
}