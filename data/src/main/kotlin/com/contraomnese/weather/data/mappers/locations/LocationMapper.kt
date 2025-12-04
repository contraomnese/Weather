package com.contraomnese.weather.data.mappers.locations

import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.MatchingLocationNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude

fun ForecastLocationNetwork.toEntity(locationId: Int): ForecastLocationEntity {
    val forecastLocationEntity = ForecastLocationEntity(
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
    return forecastLocationEntity
}

fun MatchingLocationNetwork.toEntity() = MatchingLocationEntity(
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

fun MatchingLocationEntity.toDomain() =
    Location(
        id = networkId,
        city = city,
        state = state,
        country = country,
        geo = LocationCoordinates(
            latitude = Latitude(value = latitude),
            longitude = Longitude(value = longitude)
        )
    )

fun List<MatchingLocationNetwork>.toEntity() = try {
    filter { it.type == "city" || it.type == "town" }
        .ifEmpty { this }
        .map { it.toEntity() }
} catch (cause: Exception) {
    throw operationFailed(logPrefix("Impossible convert matching locations from network"), cause)
}

fun List<MatchingLocationEntity>.toDomain() = try {
    map { it.toDomain() }
} catch (cause: Exception) {
    throw operationFailed(logPrefix("Impossible convert matching locations from storage"), cause)
}