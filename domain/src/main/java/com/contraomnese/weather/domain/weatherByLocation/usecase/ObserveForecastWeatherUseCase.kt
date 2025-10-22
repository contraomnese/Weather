package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCaseWithRequest
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.flow.Flow

class ObserveForecastWeatherUseCase(
    private val repository: ForecastWeatherRepository,
) : StreamingUseCaseWithRequest<Int, Forecast?> {

    override fun invoke(request: Int): Flow<Forecast?> = repository.observeBy(request)

}