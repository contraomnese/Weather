package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.toDomain
import com.contraomnese.weather.data.storage.db.locations.LocationsDatabase
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.locationForecast.model.LocationForecastDomainModel

class LocationsRepositoryImpl(
    private val database: LocationsDatabase,
) : LocationsRepository {

    override fun getLocationsBy(name: String): List<LocationDomainModel> {
        return try {
            database.locationsDao().getLocationsBy("$name%").map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw UnknownDomainException(throwable)
        }
    }

    override fun getLocationBy(id: Int): LocationForecastDomainModel {
        return try {
            database.locationsDao().getLocationBy(id).toDomain()
        } catch (throwable: Throwable) {
            throw UnknownDomainException(throwable)
        }
    }

}