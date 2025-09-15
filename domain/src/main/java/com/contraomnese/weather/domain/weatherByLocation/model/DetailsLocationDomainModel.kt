package com.contraomnese.weather.domain.weatherByLocation.model


@JvmInline
value class LatitudeDomainModel(val value: Double)

@JvmInline
value class LongitudeDomainModel(val value: Double)

data class DetailsLocationDomainModel(
    val id: Int,
    val name: String,
    val point: CoordinatesDomainModel,
) {
    companion object {
        val EMPTY = DetailsLocationDomainModel(
            id = 0,
            name = "",
            point = CoordinatesDomainModel(
                latitude = LatitudeDomainModel(value = .0),
                longitude = LongitudeDomainModel(value = .0)
            )
        )
    }

    fun toPoint(): String = "${point.latitude.value},${point.longitude.value}"
}

data class CoordinatesDomainModel(
    val latitude: LatitudeDomainModel,
    val longitude: LongitudeDomainModel,
)

