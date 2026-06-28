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
    val temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
    val pressureUnit: PressureUnit = PressureUnit.MmHg,
    val precipitationUnit: PrecipitationUnit = PrecipitationUnit.Millimeters,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.Kph,
    val forecastAutoSyncEnabled: Boolean = false,
    val pushNotificationsEnabled: Boolean = false,
) : MviState {

    fun update(appSettings: AppSettings): AppSettingsScreenState =
        copy(
            isLoading = false,
            temperatureUnit = appSettings.temperatureUnit,
            pressureUnit = appSettings.pressureUnit,
            precipitationUnit = appSettings.precipitationUnit,
            windSpeedUnit = appSettings.windSpeedUnit,
            forecastAutoSyncEnabled = appSettings.favoritesForecastUpdateEnabled,
            pushNotificationsEnabled = appSettings.favoritesForecastNotificationEnabled
        )

    fun setTemperatureUnit(temperatureUnit: TemperatureUnit): AppSettingsScreenState =
        copy(temperatureUnit = temperatureUnit)

    fun setPressureUnit(pressureUnit: PressureUnit): AppSettingsScreenState =
        copy(pressureUnit = pressureUnit)

    fun setPrecipitationUnit(precipitationUnit: PrecipitationUnit): AppSettingsScreenState =
        copy(precipitationUnit = precipitationUnit)

    fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit): AppSettingsScreenState =
        copy(windSpeedUnit = windSpeedUnit)

    fun setForecastAutoSync(enabled: Boolean): AppSettingsScreenState =
        copy(forecastAutoSyncEnabled = enabled)

    fun setPushNotifications(enabled: Boolean): AppSettingsScreenState =
        copy(pushNotificationsEnabled = enabled)

    companion object {
        val DEFAULT = AppSettingsScreenState(isLoading = true)
    }
}