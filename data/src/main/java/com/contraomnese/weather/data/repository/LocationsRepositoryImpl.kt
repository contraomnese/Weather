package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.toDomain
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationsRepositoryImpl(
    private val weatherDatabase: WeatherDatabase,
) : LocationsRepository {

    override fun getFavorites(): List<DetailsLocationDomainModel> {
        return try {
            weatherDatabase.favoritesDao().getFavorites().map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun observeFavorites(): Flow<List<DetailsLocationDomainModel>> =
        weatherDatabase.favoritesDao().observeFavorites().map { list -> list.map { it.toDomain() } }

    override fun addFavorite(id: Int) {
        try {
            weatherDatabase.favoritesDao().addFavorite(FavoriteEntity(cityId = id))
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

    override fun getLocationsBy(name: String): List<MatchingLocationDomainModel> {
        return try {
            weatherDatabase.locationsDao().getLocationsBy("$name%").map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun getLocationBy(id: Int): DetailsLocationDomainModel {
        return try {
            weatherDatabase.locationsDao().getLocationBy(id).toDomain()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}