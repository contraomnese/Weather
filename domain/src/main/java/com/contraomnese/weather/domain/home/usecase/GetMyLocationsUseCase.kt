package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withoutRequest.BackgroundExecutingUseCase
import com.contraomnese.weather.domain.home.model.MyLocationDomainModel
import com.contraomnese.weather.domain.home.repository.MyLocationsRepository


class GetMyLocationsUseCase(
    private val repository: MyLocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCase<List<MyLocationDomainModel>>(coroutineContextProvider) {

    override suspend fun executeInBackground(): List<MyLocationDomainModel> = repository.getLocations()
}