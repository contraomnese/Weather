package com.contraomnese.weather.data

import com.contraomnese.weather.data.network.responses.WeatherApiForecastResponse
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastData
import com.contraomnese.weather.domain.generateFake

object ForecastDataFixtures {

    fun generateRandomForecastData() = generateFake(ForecastData::class) as ForecastData

    fun generateRandomForecastNetwork() = generateFake(WeatherApiForecastResponse::class) as WeatherApiForecastResponse

    fun generateReal() = MockForecastDataReal.take()

}