package com.contraomnese.weather.domain.weatherByLocation.model


data class LocationInfoDomainModel(
    val id: Int,
    val name: String? = null,
    val countryName: String? = null,
    val point: CoordinatesDomainModel,
) {
    companion object {
        val EMPTY = LocationInfoDomainModel(
            id = 0,
            name = null,
            countryName = null,
            point = CoordinatesDomainModel(
                latitude = LatitudeDomainModel(value = .0),
                longitude = LongitudeDomainModel(value = .0)
            )
        )
    }

    fun toPoint(): String = "${point.latitude.value},${point.longitude.value}"
}

@JvmInline
value class LatitudeDomainModel(val value: Double)

@JvmInline
value class LongitudeDomainModel(val value: Double)

data class CoordinatesDomainModel(
    val latitude: LatitudeDomainModel,
    val longitude: LongitudeDomainModel,
)
