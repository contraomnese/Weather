package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveForecastsWeatherUseCase {
    operator fun invoke(request: List<Int>): Flow<List<Forecast>>
}

class ObserveForecastsWeatherUseCaseImpl(
    private val repository: ForecastRepository,
) : ObserveForecastsWeatherUseCase {

    override fun invoke(request: List<Int>): Flow<List<Forecast>> = repository.observeForecastsByLocationIds(request)

}