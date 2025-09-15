package com.contraomnese.weather.domain.home.model


@JvmInline
value class LocationPresentation(val value: String) {
    fun isValidLocation(): Boolean {
        val cityRegex = "^[\\p{L}\\s\\-'.,]*\$".toRegex()
        return value.matches(cityRegex)
    }
}

data class MatchingLocationDomainModel(
    val id: Int,
    val name: String,
    val countryName: String,
    val isFavorite: Boolean,
)


