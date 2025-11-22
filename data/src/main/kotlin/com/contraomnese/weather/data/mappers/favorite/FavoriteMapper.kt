package com.contraomnese.weather.data.mappers.favorite

import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude

fun FavoriteEntity.toDomain() = Location(
    id = locationId,
    city = city,
    state = state,
    country = country,
    geo = LocationCoordinates(
        latitude = Latitude(value = latitude),
        longitude = Longitude(value = longitude)
    )
)

fun MatchingLocationEntity.toEntity() =
    FavoriteEntity(
        locationId = networkId,
        city = city,
        state = state,
        country = country,
        latitude = latitude,
        longitude = longitude
    )

