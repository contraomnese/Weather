package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.home.model.CityDomainModel

interface LocationsRepository {

    fun getLocationsBy(name: String): List<CityDomainModel>
    fun getLocationBy(id: String): CityDomainModel
}