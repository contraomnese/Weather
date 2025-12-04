package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location

fun interface GetLocationsUseCase {
    suspend operator fun invoke(request: String): Result<List<Location>>
}


class GetLocationsUseCaseImpl(
    private val repository: LocationsRepository,
) : GetLocationsUseCase {

    override suspend fun invoke(request: String): Result<List<Location>> = repository.getLocationsByName(request)
}