package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface EnableFavoritesForecastUpdateUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class EnableFavoritesForecastUpdateUseCaseImpl(
    private val repository: AppSettingsRepository,
) : EnableFavoritesForecastUpdateUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setFavoritesForecastUpdateEnabled(true)
}