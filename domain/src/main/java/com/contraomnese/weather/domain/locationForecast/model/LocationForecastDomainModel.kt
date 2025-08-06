package com.contraomnese.weather.domain.locationForecast.model


@JvmInline
value class LatitudeDomainModel(val value: Double)

@JvmInline
value class LongitudeDomainModel(val value: Double)

data class LocationForecastDomainModel(
    val id: Int,
    val name: String,
    val point: LocationDomainPointModel,
) {
    companion object {
        val EMPTY = LocationForecastDomainModel(
            id = 0,
            name = "",
            point = LocationDomainPointModel(
                latitude = LatitudeDomainModel(value = .0),
                longitude = LongitudeDomainModel(value = .0)
            )
        )
    }

    fun getPoint(): String = "${point.latitude.value},${point.longitude.value}"
}

data class LocationDomainPointModel(
    val latitude: LatitudeDomainModel,
    val longitude: LongitudeDomainModel,
)

