package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.home.model.LocationDomainModel

interface LocationsRepository {

    fun getLocationsBy(name: String): List<LocationDomainModel>
    fun getLocationBy(id: String): LocationDomainModel
}