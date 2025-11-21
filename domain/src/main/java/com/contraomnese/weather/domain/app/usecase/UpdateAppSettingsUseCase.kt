package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface UpdateAppSettingsUseCase {
    suspend operator fun invoke(request: AppSettings): Result<Unit>
}

class UpdateAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : UpdateAppSettingsUseCase {
    override suspend fun invoke(request: AppSettings): Result<Unit> = repository.updateSettings(request)
}