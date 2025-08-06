package com.contraomnese.weather.domain.home.model


@JvmInline
value class CityPresentation(val value: String) {
    fun isValidCity(): Boolean {
        val cityRegex = "^[\\p{L}\\s\\-'.,]*\$".toRegex()
        return value.matches(cityRegex)
    }
}

@JvmInline
value class LatitudePresentation(val value: Double)

@JvmInline
value class LongitudePresentation(val value: Double)

data class LocationDomainModel(
    val id: Int,
    val name: String,
    val countryName: String,
)

data class LocationDomainPoint(
    val latitude: LatitudePresentation,
    val longitude: LongitudePresentation,
)

