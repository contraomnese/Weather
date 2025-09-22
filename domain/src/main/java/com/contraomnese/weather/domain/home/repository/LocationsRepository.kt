package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun getLocationsBy(name: String): List<LocationInfoDomainModel>
    fun addFavorite(location: LocationInfoDomainModel)
    fun deleteFavorite(id: Int)
    fun getFavorites(): List<LocationInfoDomainModel>
    fun observeFavorites(): Flow<List<LocationInfoDomainModel>>
}