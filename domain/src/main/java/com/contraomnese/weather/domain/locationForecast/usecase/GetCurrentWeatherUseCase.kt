package com.contraomnese.weather.domain.locationForecast.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.locationForecast.model.CurrentWeatherDomainModel
import com.contraomnese.weather.domain.locationForecast.repository.CurrentWeatherRepository

class GetCurrentWeatherUseCase(
    private val repository: CurrentWeatherRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<String, CurrentWeatherDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: String): CurrentWeatherDomainModel =
        repository.getWeatherBy(request)

}