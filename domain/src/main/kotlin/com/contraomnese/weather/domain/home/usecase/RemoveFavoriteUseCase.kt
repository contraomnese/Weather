package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository

fun interface RemoveFavoriteUseCase {
    suspend operator fun invoke(request: Int): Result<Unit>
}

class RemoveFavoriteUseCaseImpl(
    private val repository: LocationsRepository,
) : RemoveFavoriteUseCase {
    override suspend fun invoke(request: Int): Result<Unit> = repository.deleteFavorite(request)
}