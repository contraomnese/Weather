package com.contraomnese.weather.appsettings.presentation

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.presentation.architecture.MviEffect

internal sealed interface AppSettingsEffect : MviEffect {
    data class TemperatureUnitChanged(val temperatureUnit: TemperatureUnit) : AppSettingsEffect
    data class PressureUnitChanged(val pressureUnit: PressureUnit) : AppSettingsEffect
    data class PrecipitationUnitChanged(val precipitationUnit: PrecipitationUnit) : AppSettingsEffect
    data class WindSpeedUnitChanged(val windSpeedUnit: WindSpeedUnit) : AppSettingsEffect
    data class SettingsUpdated(val appSettings: AppSettings) : AppSettingsEffect
}