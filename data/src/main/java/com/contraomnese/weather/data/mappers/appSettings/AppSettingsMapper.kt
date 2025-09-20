package com.contraomnese.weather.data.mappers.appSettings

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit

fun AppSettingsEntity.toDomain(): AppSettings =
    AppSettings(
        language = Language(language),
        windSpeedUnit = WindSpeedUnit.entries.firstOrNull { it.name == speedUnit } ?: WindSpeedUnit.Kph,
        precipitationUnit = PrecipitationUnit.entries.firstOrNull { it.name == precipitationUnit } ?: PrecipitationUnit.Millimeters,
        temperatureUnit = TemperatureUnit.entries.firstOrNull { it.name == temperatureUnit } ?: TemperatureUnit.Celsius,
        pressureUnit = PressureUnit.entries.firstOrNull { it.name == pressureUnit } ?: PressureUnit.MmHg
    )

fun AppSettings.toEntity(): AppSettingsEntity =
    AppSettingsEntity(
        language = language.value,
        speedUnit = windSpeedUnit.name,
        precipitationUnit = precipitationUnit.name,
        temperatureUnit = temperatureUnit.name,
        pressureUnit = pressureUnit.name
    )