package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface DisableForecastAutoSyncOnAppSettingsUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class DisableForecastAutoSyncOnAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : DisableForecastAutoSyncOnAppSettingsUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setForecastAutoSyncEnabled(false)
}