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

        fun fromGeo(id: Int, latitude: Double, longitude: Double) = Location(
            id = id,
            geo = LocationCoordinates.from(latitude, longitude)
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
value class Latitude(val value: Double) {
    init {
        require(!value.isNaN() && value.isFinite()) { "Latitude must be a valid number" }
        require(value in -90.0..90.0) {
            "Latitude must be between -90 and 90, but was $value"
        }
    }
}

@JvmInline
value class Longitude(val value: Double) {
    init {
        require(!value.isNaN() && value.isFinite()) { "Longitude must be a valid number" }
        require(value in -180.0..180.0) {
            "Longitude must be between -180 and 180, but was $value"
        }
    }
}
