package com.contraomnese.weather.appsettings.presentation

import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.domain.app.usecase.DisableForecastAutoSyncOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.DisablePushNotificationUseCase
import com.contraomnese.weather.domain.app.usecase.EnableForecastAutoSyncOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.EnablePushNotificationUseCase
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetPrecipitationUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetPressureUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetTemperatureUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetWindSpeedUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.notInitialize
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest


internal class AppSettingsViewModel(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val setTemperatureUnitOnAppSettingsUseCase: SetTemperatureUnitOnAppSettingsUseCase,
    private val setPressureUnitOnAppSettingsUseCase: SetPressureUnitOnAppSettingsUseCase,
    private val setPrecipitationUnitOnAppSettingsUseCase: SetPrecipitationUnitOnAppSettingsUseCase,
    private val setWindSpeedUnitOnAppSettingsUseCase: SetWindSpeedUnitOnAppSettingsUseCase,
    private val enableForecastAutoSyncOnAppSettingsUseCase: EnableForecastAutoSyncOnAppSettingsUseCase,
    private val disableForecastAutoSyncOnAppSettingsUseCase: DisableForecastAutoSyncOnAppSettingsUseCase,
    private val enablePushNotificationUseCase: EnablePushNotificationUseCase,
    private val disablePushNotificationUseCase: DisablePushNotificationUseCase,
) : MviModel<AppSettingsAction, AppSettingsEffect, AppSettingsEvent, AppSettingsScreenState>(
    defaultState = AppSettingsScreenState.DEFAULT,
    tag = "AppSettingsViewModel"
) {

    override suspend fun bootstrap() {

        observeAppSettingsUseCase()
            .catch {
                push(AppSettingsEvent.HandleError(notInitialize(logPrefix("Bootstrap failed"), it)))
            }
            .collectLatest { settings ->
                push(AppSettingsEffect.SettingsUpdate(settings))
            }
    }

    override fun reducer(effect: AppSettingsEffect, previousState: AppSettingsScreenState) =
        when (effect) {
            is AppSettingsEffect.SettingsUpdate -> previousState.update(effect.appSettings)
            is AppSettingsEffect.PrecipitationUnitChanged -> previousState.setPrecipitationUnit(effect.precipitationUnit)
            is AppSettingsEffect.PressureUnitChanged -> previousState.setPressureUnit(effect.pressureUnit)
            is AppSettingsEffect.TemperatureUnitChanged -> previousState.setTemperatureUnit(effect.temperatureUnit)
            is AppSettingsEffect.WindSpeedUnitChanged -> previousState.setWindSpeedUnit(effect.windSpeedUnit)
            is AppSettingsEffect.ForecastAutoSyncChanged -> previousState.setForecastAutoSync(effect.enabled)
            is AppSettingsEffect.PushNotificationsChanged -> previousState.setPushNotifications(effect.enabled)
        }

    override suspend fun actor(action: AppSettingsAction) = when (action) {
        is AppSettingsAction.PrecipitationUnitChange -> processPrecipitationUnitChange(action.precipitationUnit)
        is AppSettingsAction.PressureUnitChange -> processPressureUnitChange(action.pressureUnit)
        is AppSettingsAction.TemperatureUnitChange -> processTemperatureUnitChange(action.temperatureUnit)
        is AppSettingsAction.WindSpeedUnitChange -> processWindSpeedUnitChange(action.windSpeedUnit)
        is AppSettingsAction.ForecastAutoSyncChange -> processForecastAutoSyncChange(action.enabled)
        is AppSettingsAction.PushNotificationsChange -> processPushNotificationChange(action.enabled)
    }

    private suspend fun processForecastAutoSyncChange(enabled: Boolean) {
        if (enabled) {
            enableForecastAutoSyncOnAppSettingsUseCase()
        } else {
            disableForecastAutoSyncOnAppSettingsUseCase()
        }
    }

    private suspend fun processPushNotificationChange(enabled: Boolean) {
        if (enabled) {
            enablePushNotificationUseCase()
        } else {
            disablePushNotificationUseCase()
        }
    }

    private suspend fun processTemperatureUnitChange(temperatureUnit: TemperatureUnit) {
        setTemperatureUnitOnAppSettingsUseCase(temperatureUnit)
    }

    private suspend fun processPressureUnitChange(pressureUnit: PressureUnit) {
        setPressureUnitOnAppSettingsUseCase(pressureUnit)
    }

    private suspend fun processPrecipitationUnitChange(precipitationUnit: PrecipitationUnit) {
        setPrecipitationUnitOnAppSettingsUseCase(precipitationUnit)
    }

    private suspend fun processWindSpeedUnitChange(windSpeedUnit: WindSpeedUnit) {
        setWindSpeedUnitOnAppSettingsUseCase(windSpeedUnit)
    }
}
