package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface DisablePushNotificationUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class DisablePushNotificationUseCaseImpl(
    private val repository: AppSettingsRepository,
) : DisablePushNotificationUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setPushNotificationEnabled(false)
}