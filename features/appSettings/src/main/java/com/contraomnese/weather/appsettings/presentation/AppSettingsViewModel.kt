package com.contraomnese.weather.appsettings.presentation

import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


internal class AppSettingsViewModel(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
) : MviModel<AppSettingsAction, AppSettingsEffect, AppSettingsEvent, AppSettingsScreenState>(
    defaultState = AppSettingsScreenState.DEFAULT,
    tag = "AppSettingsViewModel"
) {

    override suspend fun bootstrap() {
        push(AppSettingsEffect.SettingsUpdated(observeAppSettingsUseCase().first()))

        stateFlow
            .map { it.appSettings }
            .distinctUntilChanged()
            .onEach {
                updateAppSettingsUseCase(it)
            }
            .launchIn(viewModelScope)
    }

    override fun reducer(effect: AppSettingsEffect, previousState: AppSettingsScreenState) =
        when (effect) {
            is AppSettingsEffect.PrecipitationUnitChanged -> previousState.setPrecipitationUnit(effect.precipitationUnit)
            is AppSettingsEffect.PressureUnitChanged -> previousState.setPressureUnit(effect.pressureUnit)
            is AppSettingsEffect.TemperatureUnitChanged -> previousState.setTemperatureUnit(effect.temperatureUnit)
            is AppSettingsEffect.WindSpeedUnitChanged -> previousState.setWindSpeedUnit(effect.windSpeedUnit)
            is AppSettingsEffect.SettingsUpdated -> previousState.setAppSettings(effect.appSettings)
        }

    override suspend fun actor(action: AppSettingsAction) = when (action) {
        is AppSettingsAction.PrecipitationUnitChange -> push(AppSettingsEffect.PrecipitationUnitChanged(action.precipitationUnit))
        is AppSettingsAction.PressureUnitChange -> push(AppSettingsEffect.PressureUnitChanged(action.pressureUnit))
        is AppSettingsAction.TemperatureUnitChange -> push(AppSettingsEffect.TemperatureUnitChanged(action.temperatureUnit))
        is AppSettingsAction.WindSpeedUnitChange -> push(AppSettingsEffect.WindSpeedUnitChanged(action.windSpeedUnit))
    }
}