package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto
import com.contraomnese.weather.domain.home.model.LocationDomainModel

fun LocationDto.toDomain() = LocationDomainModel(
    id = id,
    name = name,
    countryName = countryName
)