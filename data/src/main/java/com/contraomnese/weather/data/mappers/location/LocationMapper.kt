package com.contraomnese.weather.data.mappers.location

import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.LocationNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude

fun ForecastLocationNetwork.toEntity(locationId: Int) = ForecastLocationEntity(
    locationId = locationId,
    name = name,
    region = region,
    country = country,
    latitude = lat,
    longitude = lon,
    localtimeEpoch = localtimeEpoch,
    localtime = localtime,
    timeZoneId = timeZoneId,
    lastUpdated = System.currentTimeMillis()
)

fun MatchingLocationEntity.toDomain() = Location(
    id = networkId,
    city = city,
    state = state,
    country = country,
    geo = LocationCoordinates(
        latitude = Latitude(value = latitude),
        longitude = Longitude(value = longitude)
    ),
)

fun LocationNetwork.toEntity() = MatchingLocationEntity(
    networkId = placeId.toInt(),
    latitude = lat.toDouble(),
    longitude = lon.toDouble(),
    type = type,
    name = displayName,
    houseNumber = address.houseNumber,
    road = address.road,
    neighbourhood = address.neighbourhood,
    suburb = address.suburb,
    island = address.island,
    city = address.city,
    county = address.county,
    state = address.state,
    stateCode = address.stateCode,
    postcode = address.postcode,
    country = address.country,
    countryCode = address.countryCode,
)