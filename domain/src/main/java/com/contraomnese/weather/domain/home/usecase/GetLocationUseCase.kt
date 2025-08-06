package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.home.repository.LocationsRepository

class GetLocationUseCase(
    private val repository: LocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<String, LocationDomainModel>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: String): LocationDomainModel = repository.getLocationBy(request)

}