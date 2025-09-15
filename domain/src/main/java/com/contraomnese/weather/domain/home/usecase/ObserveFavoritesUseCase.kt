package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCase
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(
    private val repository: LocationsRepository,
) : StreamingUseCase<List<DetailsLocationDomainModel>> {
    override fun invoke(): Flow<List<DetailsLocationDomainModel>> = repository.observeFavorites()
}