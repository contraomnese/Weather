package com.contraomnese.weather.domain.app.repository

import com.contraomnese.weather.domain.app.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    val settings: Flow<AppSettings>

    suspend fun updateSettings(settings: AppSettings)

}