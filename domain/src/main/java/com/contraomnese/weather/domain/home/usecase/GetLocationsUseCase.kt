package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
import com.contraomnese.weather.domain.home.repository.LocationsRepository


class GetLocationsUseCase(
    private val repository: LocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCaseWithRequest<String, List<MatchingLocationDomainModel>>(coroutineContextProvider) {

    override suspend fun executeInBackground(request: String): List<MatchingLocationDomainModel> =
        repository.getLocationsBy(request)
}