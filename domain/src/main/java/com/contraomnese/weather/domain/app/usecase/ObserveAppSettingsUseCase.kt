package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveAppSettingsUseCase {
    operator fun invoke(): Flow<AppSettings>
}

class ObserveAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : ObserveAppSettingsUseCase {
    override fun invoke(): Flow<AppSettings> = repository.observeSettings()
}