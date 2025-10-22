package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCase
import kotlinx.coroutines.flow.Flow

class ObserveAppSettingsUseCase(
    private val repository: AppSettingsRepository,
) : StreamingUseCase<AppSettings> {
    override fun invoke(): Flow<AppSettings> = repository.settings
}