package com.contraomnese.weather.domain.app.repository

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    fun observeSettings(): Flow<AppSettings>
    fun getSettings(): Result<AppSettings>
    suspend fun updateSettings(settings: AppSettings): Result<Unit>

    suspend fun setLanguage(language: Language): Result<Unit>
    suspend fun setTimezone(timezone: String): Result<Unit>
    suspend fun setWindSpeedUnit(unit: WindSpeedUnit): Result<Unit>
    suspend fun setPrecipitationUnit(unit: PrecipitationUnit): Result<Unit>
    suspend fun setTemperatureUnit(unit: TemperatureUnit): Result<Unit>
    suspend fun setPressureUnit(unit: PressureUnit): Result<Unit>
    suspend fun setFavoritesForecastUpdateEnabled(enabled: Boolean): Result<Unit>
    suspend fun setFavoritesForecastNotificationEnabled(enabled: Boolean): Result<Unit>
    suspend fun getFavoritesForecastNotificationEnabled(): Result<Boolean>
    suspend fun setFavoritesForecastUpdateScheduleTime(selectedTime: Long)
    suspend fun getFavoritesForecastUpdateScheduleTime(): Result<Long>
}