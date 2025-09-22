package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.favorite.toDomain
import com.contraomnese.weather.data.mappers.favorite.toEntity
import com.contraomnese.weather.data.mappers.location.toDomain
import com.contraomnese.weather.data.network.api.LocationsApi
import com.contraomnese.weather.data.network.parsers.parseOrThrowError
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LocationsRepositoryImpl(
    private val locationsApi: LocationsApi,
    private val weatherDatabase: WeatherDatabase,
) : LocationsRepository {

    override fun getFavorites(): List<LocationInfoDomainModel> {
        return try {
            weatherDatabase.favoritesDao().getFavorites().map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun observeFavorites(): Flow<List<LocationInfoDomainModel>> =
        weatherDatabase.favoritesDao().observeFavorites().map { list -> list.map { it.toDomain() } }.flowOn(Dispatchers.IO)

    override fun addFavorite(location: LocationInfoDomainModel) {
        try {
            weatherDatabase.favoritesDao().addFavorite(location.toEntity())
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun deleteFavorite(id: Int) {
        try {
            weatherDatabase.favoritesDao().removeFavorite(id)
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun getLocationsBy(name: String): List<LocationInfoDomainModel> {
        return try {
            locationsApi.getLocations(name).parseOrThrowError().filter { it.addressType == "city" }.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}