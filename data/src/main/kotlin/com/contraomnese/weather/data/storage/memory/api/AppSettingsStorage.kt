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

    suspend fun saveLanguage(language: Language)
    suspend fun saveTimezone(timezone: String)
    suspend fun saveWindSpeedUnit(unit: WindSpeedUnit)
    suspend fun savePrecipitationUnit(unit: PrecipitationUnit)
    suspend fun saveTemperatureUnit(unit: TemperatureUnit)
    suspend fun savePressureUnit(unit: PressureUnit)
    suspend fun saveFavoritesForecastUpdateEnabled(enabled: Boolean)
    suspend fun saveFavoritesForecastNotificationEnabled(enabled: Boolean)
    suspend fun readFavoritesForecastNotificationEnabled(): Boolean
    suspend fun saveFavoritesForecastUpdateInterval(selectedTime: Long)
    suspend fun readFavoritesForecastUpdateInterval(): Long
}