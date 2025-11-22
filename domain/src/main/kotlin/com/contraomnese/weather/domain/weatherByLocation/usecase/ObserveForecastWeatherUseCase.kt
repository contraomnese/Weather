package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveForecastWeatherUseCase {
    operator fun invoke(request: Int): Flow<Forecast?>
}

class ObserveForecastWeatherUseCaseImpl(
    private val repository: ForecastWeatherRepository,
) : ObserveForecastWeatherUseCase {

    override fun invoke(request: Int): Flow<Forecast?> = repository.getForecastByLocationId(request)

}