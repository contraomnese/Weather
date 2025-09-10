package com.contraomnese.weather.appsettings.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.VisibilityUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider

@Immutable
internal data class AppSettingsUiState(
    override val isLoading: Boolean = false,
    val appSettings: AppSettings = AppSettings(),
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface AppSettingsEvent {
    data class TemperatureUnitChanged(val newTemperatureUnit: TemperatureUnit) : AppSettingsEvent
    data class PressureUnitChanged(val newPressureUnit: PressureUnit) : AppSettingsEvent
    data class PrecipitationUnitChanged(val newPrecipitationUnit: PrecipitationUnit) : AppSettingsEvent
    data class VisibilityUnitChanged(val newVisibilityUnit: VisibilityUnit) : AppSettingsEvent
    data class WindSpeedUnitChanged(val newWindSpeedUnit: WindSpeedUnit) : AppSettingsEvent
}

internal class AppSettingsViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
) : BaseViewModel<AppSettingsUiState, AppSettingsEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(getAppSettingsUseCase, ::updateAppSettingsState)
    }

    override fun initialState(): AppSettingsUiState = AppSettingsUiState()

    override fun onEvent(event: AppSettingsEvent) {
        when (event) {
            is AppSettingsEvent.TemperatureUnitChanged -> onTemperatureUnitChanged(event.newTemperatureUnit)
            is AppSettingsEvent.PressureUnitChanged -> onPressureUnitChanged(event.newPressureUnit)
            is AppSettingsEvent.PrecipitationUnitChanged -> onPrecipitationUnitChanged(event.newPrecipitationUnit)
            is AppSettingsEvent.VisibilityUnitChanged -> onVisibilityUnitChanged(event.newVisibilityUnit)
            is AppSettingsEvent.WindSpeedUnitChanged -> onWindSpeedUnitChanged(event.newWindSpeedUnit)
        }
    }

    private fun updateAppSettings(newAppSettings: AppSettings) {
        execute(
            updateAppSettingsUseCase,
            newAppSettings,
            onException = ::provideException
        )
        updateAppSettingsState(newAppSettings)
    }

    private fun updateAppSettingsState(newAppSettings: AppSettings) {
        updateViewState { copy(appSettings = newAppSettings) }
    }

    private fun onTemperatureUnitChanged(newTemperatureUnit: TemperatureUnit) {
        updateAppSettings(uiState.value.appSettings.copy(temperatureUnit = newTemperatureUnit))
    }

    private fun onPressureUnitChanged(newPressureUnit: PressureUnit) {
        updateAppSettings(uiState.value.appSettings.copy(pressureUnit = newPressureUnit))
    }

    private fun onPrecipitationUnitChanged(newPrecipitationUnit: PrecipitationUnit) {
        updateAppSettings(uiState.value.appSettings.copy(precipitationUnit = newPrecipitationUnit))
    }

    private fun onVisibilityUnitChanged(newVisibilityUnit: VisibilityUnit) {
        updateAppSettings(uiState.value.appSettings.copy(visibilityUnit = newVisibilityUnit))
    }

    private fun onWindSpeedUnitChanged(newWindSpeedUnit: WindSpeedUnit) {
        updateAppSettings(uiState.value.appSettings.copy(windSpeedUnit = newWindSpeedUnit))
    }
}