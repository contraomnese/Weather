package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto
import com.contraomnese.weather.data.storage.db.locations.dto.LocationForecastDto
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.locationForecast.model.LatitudeDomainModel
import com.contraomnese.weather.domain.locationForecast.model.LocationDomainPointModel
import com.contraomnese.weather.domain.locationForecast.model.LocationForecastDomainModel
import com.contraomnese.weather.domain.locationForecast.model.LongitudeDomainModel

fun LocationDto.toDomain() = LocationDomainModel(
    id = id,
    name = name,
    countryName = countryName
)

fun LocationForecastDto.toDomain() = LocationForecastDomainModel(
    id = id,
    name = name,
    point = LocationDomainPointModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    )

)