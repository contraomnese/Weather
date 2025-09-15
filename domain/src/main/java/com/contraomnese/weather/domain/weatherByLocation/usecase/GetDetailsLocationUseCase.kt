package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel

class GetDetailsLocationUseCase(
    private val repository: LocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<Int, DetailsLocationDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: Int): DetailsLocationDomainModel =
        repository.getLocationBy(request)

}