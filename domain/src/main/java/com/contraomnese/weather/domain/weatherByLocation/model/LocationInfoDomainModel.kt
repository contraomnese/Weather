package com.contraomnese.weather.domain.weatherByLocation.model


data class LocationInfoDomainModel(
    val id: Int,
    val name: String,
    val countryName: String? = null,
    val point: CoordinatesDomainModel,
) {
    companion object {
        val EMPTY = from(0, "", .0, .0)

        fun from(id: Int, name: String, lat: Double, lon: Double) =
            LocationInfoDomainModel(
                id = id,
                name = name,
                point = CoordinatesDomainModel(LatitudeDomainModel(lat), LongitudeDomainModel(lon))
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
