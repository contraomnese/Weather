package com.contraomnese.weather.data.mappers.favorite

import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude

class FavoriteMapper {
    fun toDomain(entity: FavoriteEntity): Location = entity.let {
        Location(
            id = it.locationId,
            city = it.city,
            state = it.state,
            country = it.country,
            geo = LocationCoordinates(
                latitude = Latitude(value = it.latitude),
                longitude = Longitude(value = it.longitude)
            )
        )
    }
}
