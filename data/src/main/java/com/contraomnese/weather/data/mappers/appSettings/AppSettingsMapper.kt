package com.contraomnese.weather.data.mappers.appSettings

import com.contraomnese.weather.data.mappers.BiDirectMapper
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit


class AppSettingsMapper : BiDirectMapper<AppSettingsEntity, AppSettings> {
    override fun toDomain(entity: AppSettingsEntity): AppSettings = AppSettings(
        language = Language(entity.language),
        windSpeedUnit = WindSpeedUnit.entries.firstOrNull { it.name == entity.speedUnit } ?: WindSpeedUnit.Kph,
        precipitationUnit = PrecipitationUnit.entries.firstOrNull { it.name == entity.precipitationUnit } ?: PrecipitationUnit.Millimeters,
        temperatureUnit = TemperatureUnit.entries.firstOrNull { it.name == entity.temperatureUnit } ?: TemperatureUnit.Celsius,
        pressureUnit = PressureUnit.entries.firstOrNull { it.name == entity.pressureUnit } ?: PressureUnit.MmHg
    )

    override fun toEntity(model: AppSettings): AppSettingsEntity = AppSettingsEntity(
        language = model.language.value,
        speedUnit = model.windSpeedUnit.name,
        precipitationUnit = model.precipitationUnit.name,
        temperatureUnit = model.temperatureUnit.name,
        pressureUnit = model.pressureUnit.name
    )
}