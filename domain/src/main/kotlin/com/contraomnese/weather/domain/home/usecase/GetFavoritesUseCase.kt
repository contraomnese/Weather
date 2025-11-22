package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location

fun interface GetFavoritesUseCase {
    suspend operator fun invoke(): Result<List<Location>>
}

class GetFavoritesUseCaseImpl(
    private val repository: LocationsRepository,
) : GetFavoritesUseCase {

    override suspend fun invoke(): Result<List<Location>> =
        repository.getFavorites()

}