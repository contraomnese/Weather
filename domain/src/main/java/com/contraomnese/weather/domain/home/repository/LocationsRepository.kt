package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.weatherByLocation.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun getLocationsBy(name: String): Result<List<Location>>
    suspend fun getLocationBy(lat: Double, lon: Double): Result<Location>
    suspend fun addFavorite(locationId: Int): Result<Unit>
    suspend fun deleteFavorite(id: Int): Result<Unit>
    suspend fun getFavorites(): Result<List<Location>>
    fun observeFavorites(): Flow<List<Location>>
}