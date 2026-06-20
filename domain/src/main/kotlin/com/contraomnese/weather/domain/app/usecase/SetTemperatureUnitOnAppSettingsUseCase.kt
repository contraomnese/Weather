package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface SetTemperatureUnitOnAppSettingsUseCase {
    suspend operator fun invoke(request: TemperatureUnit): Result<Unit>
}

class SetTemperatureUnitOnAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : SetTemperatureUnitOnAppSettingsUseCase {
    override suspend fun invoke(request: TemperatureUnit): Result<Unit> = repository.setTemperatureUnit(request)
}