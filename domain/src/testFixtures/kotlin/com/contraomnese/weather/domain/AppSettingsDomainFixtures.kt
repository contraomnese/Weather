package com.contraomnese.weather.domain

import com.contraomnese.weather.domain.app.model.AppSettings

object AppSettingsDomainFixtures {

    fun generateRandom() = generateFake(AppSettings::class) as AppSettings
    fun generateReal() = AppSettings()

}