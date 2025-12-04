package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository

fun interface RemoveFavoriteUseCase {
    suspend operator fun invoke(request: Int): Result<Int>
}

class RemoveFavoriteUseCaseImpl(
    private val repository: LocationsRepository,
) : RemoveFavoriteUseCase {
    override suspend fun invoke(request: Int): Result<Int> = repository.deleteFavorite(request)
}