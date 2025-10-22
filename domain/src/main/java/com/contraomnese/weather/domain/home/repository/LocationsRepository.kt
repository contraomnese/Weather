package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.weatherByLocation.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun getLocationsBy(name: String): List<Location>
    suspend fun getLocationBy(lat: Double, lon: Double): Location
    fun addFavorite(locationId: Int)
    fun deleteFavorite(id: Int)
    fun getFavorites(): List<Location>
    fun observeFavorites(): Flow<List<Location>>
}