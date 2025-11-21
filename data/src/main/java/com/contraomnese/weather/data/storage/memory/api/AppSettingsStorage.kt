package com.contraomnese.weather.data.storage.memory.api

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import kotlinx.coroutines.flow.Flow

interface AppSettingsStorage {
    fun observeSettings(): Flow<AppSettingsEntity>
    fun getSettings(): AppSettingsEntity
    suspend fun saveSettings(entity: AppSettingsEntity)
}