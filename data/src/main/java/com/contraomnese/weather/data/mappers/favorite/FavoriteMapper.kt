package com.contraomnese.weather.data.mappers.favorite

import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel

fun FavoriteEntity.toDomain() = LocationInfoDomainModel(
    id = locationId,
    city = name,
    state = "", // TODO: add to entity
    country = countryName,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    )
)

fun MatchingLocationEntity.toEntity() = FavoriteEntity(
    locationId = networkId,
    name = city,
    countryName = country,
    latitude = latitude,
    longitude = longitude
)

