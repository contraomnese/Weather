package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.GeoLocationDomainModel

interface LocationsRepository {

    fun getLocationsBy(name: String): List<LocationDomainModel>
    fun getLocationBy(id: Int): GeoLocationDomainModel
}