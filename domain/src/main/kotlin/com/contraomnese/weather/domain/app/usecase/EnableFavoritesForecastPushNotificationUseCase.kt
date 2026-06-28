package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface EnableFavoritesForecastPushNotificationUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class EnableFavoritesForecastPushNotificationUseCaseImpl(
    private val repository: AppSettingsRepository,
) : EnableFavoritesForecastPushNotificationUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setFavoritesForecastNotificationEnabled(true)
}