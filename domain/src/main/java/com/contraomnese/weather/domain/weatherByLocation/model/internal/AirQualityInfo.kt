package com.contraomnese.weather.domain.weatherByLocation.model.internal

data class AirQualityInfo(
    val aqiIndex: Int,
    val aqiText: String,
    val coLevel: PollutantLevel,
    val no2Level: PollutantLevel,
    val o3Level: PollutantLevel,
    val so2Level: PollutantLevel,
    val pm25Level: PollutantLevel,
    val pm10Level: PollutantLevel,
)

sealed interface PollutantLevel {
    data object Good : PollutantLevel
    data object Moderate : PollutantLevel
    data object Bad : PollutantLevel
}