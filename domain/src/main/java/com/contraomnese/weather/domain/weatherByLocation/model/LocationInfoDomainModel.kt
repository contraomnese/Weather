package com.contraomnese.weather.domain.weatherByLocation.model


data class LocationInfoDomainModel(
    val id: Int,
    val city: String,
    val state: String,
    val country: String,
    val point: CoordinatesDomainModel,
) {
    companion object {
        val EMPTY = LocationInfoDomainModel(
            id = 0,
            city = "city",
            state = "",
            country = "",
            point = CoordinatesDomainModel(LatitudeDomainModel(.0), LongitudeDomainModel(.0))
        )
    }
}

@JvmInline
value class LatitudeDomainModel(val value: Double)

@JvmInline
value class LongitudeDomainModel(val value: Double)

data class CoordinatesDomainModel(
    val latitude: LatitudeDomainModel,
    val longitude: LongitudeDomainModel,
)
