package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface SetWindSpeedUnitOnAppSettingsUseCase {
    suspend operator fun invoke(request: WindSpeedUnit): Result<Unit>
}

class SetWindSpeedUnitOnAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : SetWindSpeedUnitOnAppSettingsUseCase {
    override suspend fun invoke(request: WindSpeedUnit): Result<Unit> = repository.setWindSpeedUnit(request)
}