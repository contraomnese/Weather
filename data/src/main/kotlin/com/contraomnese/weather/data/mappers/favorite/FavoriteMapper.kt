package com.contraomnese.weather.data.mappers.favorite

import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude


fun FavoriteEntity.toDomain(): Location = Location(
    id = locationId,
    city = city,
    state = state,
    country = country,
    geo = LocationCoordinates(
        latitude = Latitude(value = latitude),
        longitude = Longitude(value = longitude)
    )
)

fun List<FavoriteEntity>.toDomain() = try {
    map { it.toDomain() }
} catch (cause: Exception) {
    throw operationFailed(logPrefix("Impossible convert favorites from storage"), cause)
}
