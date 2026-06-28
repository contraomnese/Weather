package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface DisableFavoritesForecastUpdateUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class DisableFavoritesForecastUpdateUseCaseImpl(
    private val repository: AppSettingsRepository,
) : DisableFavoritesForecastUpdateUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setFavoritesForecastUpdateEnabled(false)
}