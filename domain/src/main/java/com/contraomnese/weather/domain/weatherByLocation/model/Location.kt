package com.contraomnese.weather.domain.weatherByLocation.model


data class Location(
    val id: Int,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val geo: LocationCoordinates,
) {
    companion object {
        val EMPTY = Location(
            id = 0,
            geo = LocationCoordinates.EMPTY
        )
    }
}

data class LocationCoordinates(
    val latitude: Latitude,
    val longitude: Longitude,
) {
    companion object {
        val EMPTY = LocationCoordinates(Latitude(.0), Longitude(.0))

        fun from(latitude: Double, longitude: Double) = LocationCoordinates(Latitude(latitude), Longitude(longitude))
    }
}


@JvmInline
value class Latitude(val value: Double)

@JvmInline
value class Longitude(val value: Double)
