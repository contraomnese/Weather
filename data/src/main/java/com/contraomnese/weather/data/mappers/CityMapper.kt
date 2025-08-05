package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.storage.db.locations.dto.CityDto
import com.contraomnese.weather.domain.home.model.CityDomainModel
import com.contraomnese.weather.domain.home.model.LatitudePresentation
import com.contraomnese.weather.domain.home.model.LocationDomainPoint
import com.contraomnese.weather.domain.home.model.LongitudePresentation

fun CityDto.toDomain() = CityDomainModel(
    id = id,
    name = name,
    countryId = countryId,
    point = LocationDomainPoint(
        latitude = LatitudePresentation(value = latitude),
        longitude = LongitudePresentation(value = longitude)
    )
)