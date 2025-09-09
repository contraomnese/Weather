package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.AirQuality
import com.contraomnese.weather.domain.weatherByLocation.model.AirQualityInfo
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel

fun AirQuality.toDomain(): AirQualityInfo {
    return AirQualityInfo(
        aqiIndex = gbDefraIndex,
        aqiText = when (gbDefraIndex) {
            in 1..3 -> "Low"
            in 4..6 -> "Moderate"
            in 7..9 -> "High"
            else -> "Very High"
        },
        coLevel = when (co / 1000) {
            in 0.0..4.5 -> PollutantLevel.Good
            in 4.5..9.4 -> PollutantLevel.Moderate
            else -> PollutantLevel.Bad
        },
        no2Level = when (no2) {
            in 0.0..100.0 -> PollutantLevel.Good
            in 100.0..200.0 -> PollutantLevel.Moderate
            else -> PollutantLevel.Bad
        },
        o3Level = when (o3) {
            in 0.0..100.0 -> PollutantLevel.Good
            in 100.0..160.0 -> PollutantLevel.Moderate
            else -> PollutantLevel.Bad
        },
        so2Level = when (so2) {
            in 0.0..35.0 -> PollutantLevel.Good
            in 35.0..75.0 -> PollutantLevel.Moderate
            else -> PollutantLevel.Bad
        },
        pm25Level = when (pm25) {
            in 0.0..12.0 -> PollutantLevel.Good
            in 12.0..35.4 -> PollutantLevel.Moderate
            else -> PollutantLevel.Bad
        },
        pm10Level = when (pm10) {
            in 0.0..55.0 -> PollutantLevel.Good
            in 55.0..154.0 -> PollutantLevel.Moderate
            else -> PollutantLevel.Bad
        },
    )
}