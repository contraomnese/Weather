package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    fun getLocationsBy(name: String): List<MatchingLocationDomainModel>
    fun getLocationBy(id: Int): DetailsLocationDomainModel
    fun addFavorite(id: Int)
    fun deleteFavorite(id: Int)
    fun getFavorites(): List<DetailsLocationDomainModel>
    fun observeFavorites(): Flow<List<DetailsLocationDomainModel>>
}