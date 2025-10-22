package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location


class GetLocationsUseCase(
    private val repository: LocationsRepository,
    coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCaseWithRequest<String, List<Location>>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: String): List<Location> =
        repository.getLocationsBy(request)
}