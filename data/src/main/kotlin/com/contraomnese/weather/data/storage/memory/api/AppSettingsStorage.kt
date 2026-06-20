package com.contraomnese.weather.data.storage.memory.api

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface AppSettingsStorage {
    fun observeSettings(): Flow<AppSettingsEntity>
    fun getSettings(): AppSettingsEntity
    suspend fun saveSettings(settings: AppSettingsEntity)

    suspend fun setLanguage(language: Language)
    suspend fun setTimezone(timezone: String)
    suspend fun setWindSpeedUnit(unit: WindSpeedUnit)
    suspend fun setPrecipitationUnit(unit: PrecipitationUnit)
    suspend fun setTemperatureUnit(unit: TemperatureUnit)
    suspend fun setPressureUnit(unit: PressureUnit)
    suspend fun setForecastAutoSyncEnabled(enabled: Boolean)
    suspend fun setNotificationEnabled(enabled: Boolean)
}