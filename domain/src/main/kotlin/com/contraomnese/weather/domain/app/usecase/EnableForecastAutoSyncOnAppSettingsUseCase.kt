package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface EnableForecastAutoSyncOnAppSettingsUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class EnableForecastAutoSyncOnAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : EnableForecastAutoSyncOnAppSettingsUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setForecastAutoSyncEnabled(true)
}