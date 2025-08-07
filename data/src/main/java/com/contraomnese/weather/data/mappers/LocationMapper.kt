package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto
import com.contraomnese.weather.data.storage.db.locations.dto.WeatherLocationDto
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.GeoLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel

fun LocationDto.toDomain() = LocationDomainModel(
    id = id,
    name = name,
    countryName = countryName
)

fun WeatherLocationDto.toDomain() = GeoLocationDomainModel(
    id = id,
    name = name,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    )

)