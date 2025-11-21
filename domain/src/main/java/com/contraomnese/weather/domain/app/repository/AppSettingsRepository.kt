package com.contraomnese.weather.domain.app.repository

import com.contraomnese.weather.domain.app.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    fun observeSettings(): Flow<AppSettings>

    suspend fun getSettings(): Result<AppSettings>

    suspend fun updateSettings(settings: AppSettings): Result<Unit>

}