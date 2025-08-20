package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository

class GetForecastWeatherUseCase(
    private val repository: ForecastWeatherRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<String, WeatherDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: String): WeatherDomainModel =
        repository.getBy(request)

}