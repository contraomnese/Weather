package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.Language.Companion.toLocalCode
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.VisibilityUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit

fun AppSettingsEntity.toDomain(): AppSettings =
    AppSettings(
        language = Language.entries.firstOrNull { it.name == language } ?: Language.English,
        windSpeedUnit = WindSpeedUnit.entries.firstOrNull { it.name == speedUnit } ?: WindSpeedUnit.Kph,
        precipitationUnit = PrecipitationUnit.entries.firstOrNull { it.name == precipitationUnit } ?: PrecipitationUnit.Millimeters,
        temperatureUnit = TemperatureUnit.entries.firstOrNull { it.name == temperatureUnit } ?: TemperatureUnit.Celsius,
        visibilityUnit = VisibilityUnit.entries.firstOrNull { it.name == visibilityUnit } ?: VisibilityUnit.Kilometers,
        pressureUnit = PressureUnit.entries.firstOrNull { it.name == pressureUnit } ?: PressureUnit.MmHg
    )

fun AppSettings.toEntity(): AppSettingsEntity =
    AppSettingsEntity(
        language = language.toLocalCode(),
        speedUnit = windSpeedUnit.name,
        precipitationUnit = precipitationUnit.name,
        temperatureUnit = temperatureUnit.name,
        visibilityUnit = visibilityUnit.name,
        pressureUnit = pressureUnit.name
    )