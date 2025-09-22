package com.contraomnese.weather.data.mappers.favorite

import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel

fun FavoriteEntity.toDomain() = LocationInfoDomainModel(
    id = locationId,
    name = name,
    countryName = countryName,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    )
)

fun LocationInfoDomainModel.toEntity() = FavoriteEntity(
    locationId = id,
    name = name,
    countryName = countryName ?: "",
    latitude = point.latitude.value,
    longitude = point.longitude.value
)

