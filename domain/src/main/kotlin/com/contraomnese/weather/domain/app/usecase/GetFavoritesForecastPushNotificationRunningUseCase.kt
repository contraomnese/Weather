package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface GetFavoritesForecastPushNotificationRunningUseCase {
    suspend operator fun invoke(): Result<Boolean>
}

class GetFavoritesForecastPushNotificationRunningUseCaseImpl(
    private val repository: AppSettingsRepository,
) : GetFavoritesForecastPushNotificationRunningUseCase {
    override suspend fun invoke(): Result<Boolean> = repository.getFavoritesForecastNotificationEnabled()
}