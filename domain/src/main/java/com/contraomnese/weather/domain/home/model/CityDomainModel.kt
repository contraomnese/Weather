package com.contraomnese.weather.domain.home.model


@JvmInline
value class CityPresentation(val value: String) {
    fun isValidCity(): Boolean {
        val cityRegex = "^[\\p{L}\\s\\-'.,]*\$".toRegex()
        return value.matches(cityRegex)
    }
}

@JvmInline
value class LatitudePresentation(val value: Float)

@JvmInline
value class LongitudePresentation(val value: Float)

data class CityDomainModel(
    val id: Int,
    val name: String,
    val countryId: Int,
    val point: LocationDomainPoint,
)

data class LocationDomainPoint(
    val latitude: LatitudePresentation,
    val longitude: LongitudePresentation,
)

