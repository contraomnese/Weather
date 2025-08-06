package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto
import com.contraomnese.weather.domain.home.model.CityDomainModel
import com.contraomnese.weather.domain.home.model.LatitudePresentation
import com.contraomnese.weather.domain.home.model.LocationDomainPoint
import com.contraomnese.weather.domain.home.model.LongitudePresentation

fun LocationDto.toDomain() = CityDomainModel(
    id = id,
    name = name,
    countryName = countryName,
    point = LocationDomainPoint(
        latitude = LatitudePresentation(value = latitude),
        longitude = LongitudePresentation(value = longitude)
    )
)