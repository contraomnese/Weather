package com.contraomnese.weather.data.mappers.location

import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.LocationNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
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

fun LocationNetwork.toDomain() = LocationInfoDomainModel(
    id = placeId.toInt(),
    name = name,
    countryName = address?.country,
    point = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(value = lat.toDouble()),
        longitude = LongitudeDomainModel(value = lon.toDouble())
    ),
)