package com.contraomnese.weather.data.mappers.location

import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.LocationNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel

fun ForecastLocationNetwork.toEntity(locationId: Int) = ForecastLocationEntity(
    locationId = locationId,
    name = name,
    country = country,
    latitude = lat,
    longitude = lon,
    localtimeEpoch = localtimeEpoch,
    localtime = localtime,
    timeZoneId = timeZoneId,
    lastUpdated = System.currentTimeMillis()
)

fun MatchingLocationEntity.toDomain() = LocationInfoDomainModel(
    id = networkId,
    city = city,
    state = state,
    country = country,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = latitude),
        longitude = LongitudeDomainModel(value = longitude)
    ),
)

fun LocationNetwork.toEntity() = MatchingLocationEntity(
    networkId = placeId.toInt(),
    latitude = lat.toDouble(),
    longitude = lon.toDouble(),
    type = type ?: "",
    name = address.name ?: "",
    houseNumber = address.houseNumber ?: "",
    road = address.road ?: "",
    neighbourhood = address.neighbourhood ?: "",
    suburb = address.suburb ?: "",
    island = address.island ?: "",
    city = address.city ?: "",
    county = address.county ?: "",
    state = address.state ?: "",
    stateCode = address.stateCode ?: "",
    postcode = address.postcode ?: "",
    country = address.country ?: "",
    countryCode = address.countryCode ?: "",
)