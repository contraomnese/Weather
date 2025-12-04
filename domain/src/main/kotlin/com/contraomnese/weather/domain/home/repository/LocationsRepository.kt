package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.weatherByLocation.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun getLocationsByName(query: String): Result<List<Location>>
    suspend fun getLocationByCoordinates(latitude: Double, longitude: Double): Result<Location>
    suspend fun addFavorite(locationId: Int): Result<Int>
    suspend fun deleteFavorite(locationId: Int): Result<Int>
    suspend fun getFavorites(): Result<List<Location>>
    fun observeFavorites(): Flow<List<Location>>
}