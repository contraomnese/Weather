package com.contraomnese.weather.domain

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import kotlin.random.Random

object AppSettingsDomainFixtures {

    private val random = Random(1234)
    private val languages = listOf("en", "de", "fr", "ru")
    private val windSpeedUnits = WindSpeedUnit.entries
    private val precipitationUnits = PrecipitationUnit.entries
    private val temperatureUnits = TemperatureUnit.entries
    private val pressureUnits = PressureUnit.entries

    fun generate() =
        AppSettings(
            language = randomLanguage(),
            windSpeedUnit = randomWindSpeedUnit(),
            precipitationUnit = randomPrecipitationUnit(),
            temperatureUnit = randomTemperatureUnit(),
            pressureUnit = randomPressureUnit()
        )

    private fun randomLanguage() = Language(languages.random(random))
    private fun randomWindSpeedUnit() = windSpeedUnits.random(random)
    private fun randomPrecipitationUnit() = precipitationUnits.random(random)
    private fun randomTemperatureUnit() = temperatureUnits.random(random)
    private fun randomPressureUnit() = pressureUnits.random(random)

}