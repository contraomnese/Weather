package com.contraomnese.weather.domain.locationForecast.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.locationForecast.model.LocationForecastDomainModel

class GetLocationForecastUseCase(
    private val repository: LocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<Int, LocationForecastDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: Int): LocationForecastDomainModel =
        repository.getLocationBy(request)

}