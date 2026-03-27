package com.contraomnese.weather.data.mappers.locations

import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude
import com.contraomnese.weather.data.network.models.locationiq.LocationNetwork as IQLocationNetwork
import com.contraomnese.weather.data.network.models.openweather.geolocation.LocationNetwork as OWLocationNetwork

private const val UNKNOWN = "Unknown"

fun IQLocationNetwork.toEntity() = MatchingLocationEntity(
    networkId = placeId.toInt(),
    latitude = lat.toDouble(),
    longitude = lon.toDouble(),
    name = displayName,
    city = address.city,
    state = address.state,
    country = address.country,
    countryCode = address.countryCode,
)

fun OWLocationNetwork.toEntity() = MatchingLocationEntity(
    networkId = id,
    latitude = latitude,
    longitude = longitude,
    name = name,
    city = admin2,
    state = admin1,
    country = country,
    countryCode = countryCode,
    timeZoneId = timezone
)

fun MatchingLocationEntity.toForecastLocationEntity(locationId: Int) = ForecastLocationEntity(
    locationId = locationId,
    latitude = latitude,
    longitude = longitude,
    name = name,
    city = city ?: UNKNOWN,
    region = state ?: UNKNOWN,
    country = country ?: UNKNOWN,
    timeZoneId = timeZoneId ?: "GMT",
    lastUpdated = System.currentTimeMillis()
)

fun MatchingLocationEntity.toDomain() =
    Location(
        id = networkId,
        name = name,
        city = city,
        state = state,
        countryCode = countryCode ?: UNKNOWN,
        geo = LocationCoordinates(
            latitude = Latitude(value = latitude),
            longitude = Longitude(value = longitude)
        )
    )

fun List<IQLocationNetwork>.toEntities() = try {
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