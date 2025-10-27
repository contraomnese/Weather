package com.contraomnese.weather.appsettings.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.presentation.architecture.MviState

@Immutable
internal data class AppSettingsScreenState(
    override val isLoading: Boolean = false,
    val appSettings: AppSettings = AppSettings(),
) : MviState {
    fun setAppSettings(appSettings: AppSettings): AppSettingsScreenState = copy(appSettings = appSettings, isLoading = false)

    fun setTemperatureUnit(temperatureUnit: TemperatureUnit): AppSettingsScreenState =
        copy(appSettings = appSettings.copy(temperatureUnit = temperatureUnit))

    fun setPressureUnit(pressureUnit: PressureUnit): AppSettingsScreenState =
        copy(appSettings = appSettings.copy(pressureUnit = pressureUnit))

    fun setPrecipitationUnit(precipitationUnit: PrecipitationUnit): AppSettingsScreenState =
        copy(appSettings = appSettings.copy(precipitationUnit = precipitationUnit))

    fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit): AppSettingsScreenState =
        copy(appSettings = appSettings.copy(windSpeedUnit = windSpeedUnit))

    companion object {
        val DEFAULT = AppSettingsScreenState(isLoading = true)
    }
}