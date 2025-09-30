package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel


class GetLocationUseCase(
    private val repository: LocationsRepository,
    coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCaseWithRequest<CoordinatesDomainModel, LocationInfoDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: CoordinatesDomainModel): LocationInfoDomainModel =
        repository.getLocationBy(lat = request.latitude.value, lon = request.longitude.value)
}