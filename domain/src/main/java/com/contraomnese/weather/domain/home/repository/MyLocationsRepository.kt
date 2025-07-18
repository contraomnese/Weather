package com.contraomnese.weather.domain.home.repository

import com.contraomnese.weather.domain.home.model.MyLocationDomainModel

interface MyLocationsRepository {

    fun getLocations(): List<MyLocationDomainModel>
    fun getLocationBy(id: String): MyLocationDomainModel
}