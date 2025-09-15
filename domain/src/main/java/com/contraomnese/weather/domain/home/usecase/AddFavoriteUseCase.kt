package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.background.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.repository.LocationsRepository

class AddFavoriteUseCase(
    private val repository: LocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<Int, Unit>(coroutineContextProvider) {
    override suspend fun executeInBackground(request: Int) = repository.addFavorite(request)
}