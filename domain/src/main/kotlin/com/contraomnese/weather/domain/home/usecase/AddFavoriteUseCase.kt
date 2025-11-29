package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository

fun interface AddFavoriteUseCase {
    suspend operator fun invoke(request: Int): Result<Int>
}

class AddFavoriteUseCaseImpl(
    private val repository: LocationsRepository,
) : AddFavoriteUseCase {
    override suspend fun invoke(request: Int): Result<Int> = repository.addFavorite(request)
}