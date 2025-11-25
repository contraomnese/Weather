package com.contraomnese.weather.data

import com.contraomnese.weather.data.network.models.ForecastResponse
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastData
import com.contraomnese.weather.domain.generateFake

object ForecastDataFixtures {

    fun generateRandomForecastData() = generateFake(ForecastData::class) as ForecastData

    fun generateRandomForecastNetwork() = generateFake(ForecastResponse::class) as ForecastResponse

    fun generateReal() = MockForecastDataReal.take()

}