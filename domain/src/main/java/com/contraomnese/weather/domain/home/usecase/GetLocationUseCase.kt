package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates

fun interface GetLocationUseCase {
    suspend operator fun invoke(request: LocationCoordinates): Result<Location>
}

class GetLocationUseCaseImpl(
    private val repository: LocationsRepository,
) : GetLocationUseCase {

    override suspend fun invoke(request: LocationCoordinates): Result<Location> =
        repository.getLocationBy(lat = request.latitude.value, lon = request.longitude.value)
}