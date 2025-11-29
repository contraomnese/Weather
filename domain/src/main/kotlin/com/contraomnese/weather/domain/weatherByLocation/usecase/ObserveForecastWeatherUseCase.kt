package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveForecastWeatherUseCase {
    operator fun invoke(request: Int): Flow<Forecast?>
}

class ObserveForecastWeatherUseCaseImpl(
    private val repository: ForecastRepository,
) : ObserveForecastWeatherUseCase {

    override fun invoke(request: Int): Flow<Forecast?> = repository.getForecastByLocationId(request)

}