package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withoutRequest.BackgroundExecutingUseCase
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location

class GetFavoritesUseCase(
    private val repository: LocationsRepository,
    coroutineContextProvider: CoroutineContextProvider,
) :
    BackgroundExecutingUseCase<List<Location>>(coroutineContextProvider) {

    override suspend fun executeInBackground(): List<Location> =
        repository.getFavorites()
}