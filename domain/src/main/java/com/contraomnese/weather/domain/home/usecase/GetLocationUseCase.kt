package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates


class GetLocationUseCase(
    private val repository: LocationsRepository,
    coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCaseWithRequest<LocationCoordinates, Location>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: LocationCoordinates): Location =
        repository.getLocationBy(lat = request.latitude.value, lon = request.longitude.value)
}