package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface DisableFavoritesForecastPushNotificationUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class DisableFavoritesForecastPushNotificationUseCaseImpl(
    private val repository: AppSettingsRepository,
) : DisableFavoritesForecastPushNotificationUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setFavoritesForecastNotificationEnabled(false)
}