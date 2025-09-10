package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest

class UpdateAppSettingsUseCase(
    private val repository: AppSettingsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<AppSettings, Unit>(coroutineContextProvider) {
    override suspend fun executeInBackground(request: AppSettings) = repository.updateSettings(request)
}