package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveSingleForecastUseCase {
    operator fun invoke(request: Int): Flow<Forecast?>
}

class ObserveSingleForecastUseCaseImpl(
    private val repository: ForecastRepository,
) : ObserveSingleForecastUseCase {

    override fun invoke(request: Int): Flow<Forecast?> = repository.observeSingleForecast(request)

}