package com.contraomnese.weather.data.stubs

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit

class AppSettingsStub {
    companion object {
        fun generate(
            language: String = "en",
            windSpeedUnit: WindSpeedUnit = WindSpeedUnit.Ms,
            precipitationUnit: PrecipitationUnit = PrecipitationUnit.Inches,
            temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
            pressureUnit: PressureUnit = PressureUnit.GPa,
        ) =
            AppSettings(
                language = Language(language),
                windSpeedUnit = windSpeedUnit,
                precipitationUnit = precipitationUnit,
                temperatureUnit = temperatureUnit,
                pressureUnit = pressureUnit
            )

        fun map(settings: AppSettings) = AppSettingsEntity(
            language = settings.language.value,
            speedUnit = settings.windSpeedUnit.name,
            precipitationUnit = settings.precipitationUnit.name,
            temperatureUnit = settings.temperatureUnit.name,
            pressureUnit = settings.pressureUnit.name
        )
    }
}