package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import kotlinx.coroutines.flow.Flow

fun interface ObserveFavoritesUseCase {
    operator fun invoke(): Flow<List<Location>>
}

class ObserveFavoritesUseCaseImpl(
    private val repository: LocationsRepository,
) : ObserveFavoritesUseCase {
    override fun invoke(): Flow<List<Location>> = repository.observeFavorites()
}