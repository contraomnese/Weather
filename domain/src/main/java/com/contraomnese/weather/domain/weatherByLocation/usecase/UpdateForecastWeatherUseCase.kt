package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository

class UpdateForecastWeatherUseCase(
    private val repository: ForecastWeatherRepository,
    coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<Int, Unit>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: Int) =
        repository.updateBy(request)

}