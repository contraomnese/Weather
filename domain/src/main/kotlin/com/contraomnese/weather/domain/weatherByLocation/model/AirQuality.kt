package com.contraomnese.weather.domain.weatherByLocation.model

data class AirQuality(
    val aqiIndex: PollutantLevel,
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
    data object UnhealthyForSensGroups : PollutantLevel
    data object Unhealthy : PollutantLevel
    data object VeryUnhealthy : PollutantLevel
    data object Hazardous : PollutantLevel
}