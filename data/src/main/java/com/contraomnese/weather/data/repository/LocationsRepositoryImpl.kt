package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.toDomain
import com.contraomnese.weather.data.storage.db.locations.LocationsDatabase
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.home.repository.LocationsRepository

class LocationsRepositoryImpl(
    private val database: LocationsDatabase,
) : LocationsRepository {

    override fun getLocationsBy(name: String): List<LocationDomainModel> {
        return try {
            database.locationsDao().getLocations("$name%").map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw UnknownDomainException(throwable)
        }
    }

    override fun getLocationBy(name: String): LocationDomainModel {
        TODO()
    }

}