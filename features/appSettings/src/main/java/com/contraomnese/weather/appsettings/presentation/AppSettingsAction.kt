package com.contraomnese.weather.appsettings.presentation

import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.presentation.architecture.MviAction

internal sealed interface AppSettingsAction : MviAction {
    data class TemperatureUnitChange(val temperatureUnit: TemperatureUnit) : AppSettingsAction
    data class PressureUnitChange(val pressureUnit: PressureUnit) : AppSettingsAction
    data class PrecipitationUnitChange(val precipitationUnit: PrecipitationUnit) : AppSettingsAction
    data class WindSpeedUnitChange(val windSpeedUnit: WindSpeedUnit) : AppSettingsAction

}