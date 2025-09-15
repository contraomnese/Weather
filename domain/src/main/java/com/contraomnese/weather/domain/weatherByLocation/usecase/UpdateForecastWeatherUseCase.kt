package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository

class UpdateForecastWeatherUseCase(
    private val repository: ForecastWeatherRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<DetailsLocationDomainModel, Unit>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: DetailsLocationDomainModel) =
        repository.updateBy(request)

}