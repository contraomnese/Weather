package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.GeoLocationDomainModel

class GetGeoLocationUseCase(
    private val repository: LocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<Int, GeoLocationDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: Int): GeoLocationDomainModel =
        repository.getLocationBy(request)

}