package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface SetPrecipitationUnitOnAppSettingsUseCase {
    suspend operator fun invoke(request: PrecipitationUnit): Result<Unit>
}

class SetPrecipitationUnitOnAppSettingsUseCaseImpl(
    private val repository: AppSettingsRepository,
) : SetPrecipitationUnitOnAppSettingsUseCase {
    override suspend fun invoke(request: PrecipitationUnit): Result<Unit> = repository.setPrecipitationUnit(request)
}