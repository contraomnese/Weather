package com.contraomnese.weather.data

import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastData
import com.contraomnese.weather.domain.generateFake

object ForecastDataFixtures {

    fun generateRandom() = generateFake(ForecastData::class) as ForecastData

}