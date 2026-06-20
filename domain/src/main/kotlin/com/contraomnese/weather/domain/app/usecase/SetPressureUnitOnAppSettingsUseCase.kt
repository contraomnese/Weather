package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface SetPressureUnitOnAppSettingsUseCase {
    suspend operator fun invoke(request: PressureUnit): Result<Unit>
}

class SetPressureUnitOnAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : SetPressureUnitOnAppSettingsUseCase {
    override suspend fun invoke(request: PressureUnit): Result<Unit> = repository.setPressureUnit(request)
}