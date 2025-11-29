package com.contraomnese.weather.data

import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.domain.generateFake

object ForecastLocationDataFixtures {

    fun generateRandom() = generateFake(ForecastLocationEntity::class) as ForecastLocationEntity

}