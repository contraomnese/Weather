package com.contraomnese.weather.data.mappers.location

import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastLocationDto
import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto
import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel

fun LocationDto.toDomain() = MatchingLocationDomainModel(
    id = id,
    name = name,
    countryName = countryName,
    isFavorite = isFavorite
)

fun ForecastLocationDto.toDomain() = DetailsLocationDomainModel(
    id = id,
    name = name,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    )
)

fun ForecastLocationNetwork.toEntity(locationId: Int) = ForecastLocationEntity(
    locationId = locationId,
    localtimeEpoch = localtimeEpoch,
    localtime = localtime,
    timeZoneId = timeZoneId,
    lastUpdated = System.currentTimeMillis()
)