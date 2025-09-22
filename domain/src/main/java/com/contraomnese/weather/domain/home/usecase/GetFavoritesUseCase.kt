package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withoutRequest.BackgroundExecutingUseCase
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel

class GetFavoritesUseCase(
    private val repository: LocationsRepository,
    coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCase<List<LocationInfoDomainModel>>(coroutineContextProvider) {

    override suspend fun executeInBackground(): List<LocationInfoDomainModel> =
        repository.getFavorites()
}