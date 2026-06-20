package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.repository.AppSettingsRepository

fun interface EnablePushNotificationUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class EnablePushNotificationUseCaseImpl(
    private val repository: AppSettingsRepository,
) : EnablePushNotificationUseCase {
    override suspend fun invoke(): Result<Unit> = repository.setPushNotificationEnabled(true)
}