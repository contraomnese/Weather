package com.contraomnese.weather.data

import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.AppSettings

object AppSettingsDataFixtures {

    fun map(settings: AppSettings) = AppSettingsEntity(
        language = settings.language.value,
        speedUnit = settings.windSpeedUnit.name,
        precipitationUnit = settings.precipitationUnit.name,
        temperatureUnit = settings.temperatureUnit.name,
        pressureUnit = settings.pressureUnit.name
    )
}