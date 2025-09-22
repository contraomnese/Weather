package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository

class RemoveFavoriteUseCase(
    private val repository: LocationsRepository,
    coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<Int, Unit>(coroutineContextProvider) {
    override suspend fun executeInBackground(request: Int) = repository.deleteFavorite(request)
}