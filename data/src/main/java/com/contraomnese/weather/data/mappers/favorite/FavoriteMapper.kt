package com.contraomnese.weather.data.mappers.favorite

import com.contraomnese.weather.data.storage.db.locations.dto.FavoriteDto
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel

fun FavoriteDto.toDomain() = DetailsLocationDomainModel(
    id = id,
    name = name,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    )
)

fun DetailsLocationDomainModel.toEntity() = FavoriteEntity(
    cityId = id
)