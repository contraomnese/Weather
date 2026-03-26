package com.contraomnese.weather.data.mappers.forecast.internal

import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import com.contraomnese.weather.domain.weatherByLocation.model.AirQuality
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel.Good
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel.Hazardous
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel.Moderate
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel.Unhealthy
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel.UnhealthyForSensGroups
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel.VeryUnhealthy

internal fun ForecastTodayEntity.toAirQualityUKIndex(): AirQuality {
    requireNotNull(airQualityUKIndex)
    return AirQuality(
        aqiIndex = when (airQualityUKIndex) {
            in 1..3 -> Good
            in 4..6 -> Moderate
            in 7..9 -> VeryUnhealthy
            else -> Hazardous
        },
        coLevel = when (airQualityCo / 1000) {
            in 0.0..4.5 -> Good
            in 4.5..9.4 -> Moderate
            else -> Hazardous
        },
        no2Level = when (airQualityNo2) {
            in 0.0..100.0 -> Good
            in 100.0..200.0 -> Moderate
            else -> Hazardous
        },
        o3Level = when (airQualityO3) {
            in 0.0..100.0 -> Good
            in 100.0..160.0 -> Moderate
            else -> Hazardous
        },
        so2Level = when (airQualitySo2) {
            in 0.0..35.0 -> Good
            in 35.0..75.0 -> Moderate
            else -> Hazardous
        },
        pm25Level = when (airQualityPm25) {
            in 0.0..12.0 -> Good
            in 12.0..35.4 -> Moderate
            else -> Hazardous
        },
        pm10Level = when (airQualityPm10) {
            in 0.0..55.0 -> Good
            in 55.0..154.0 -> Moderate
            else -> Hazardous
        },
    )
}

internal fun ForecastTodayEntity.toAirQualityUSAIndex(): AirQuality {
    requireNotNull(airQualityUSAIndex)
    return AirQuality(
        aqiIndex = when (airQualityUSAIndex) {
            in 0..50 -> Good
            in 51..100 -> Moderate
            in 101..150 -> UnhealthyForSensGroups
            in 151..200 -> Unhealthy
            in 201..300 -> VeryUnhealthy
            else -> Hazardous
        },
        coLevel = when (airQualityCo) {
            in 0.0..4.5 -> Good
            in 4.5..9.5 -> Moderate
            in 9.5..12.5 -> UnhealthyForSensGroups
            in 12.5..15.5 -> Unhealthy
            in 15.5..30.5 -> VeryUnhealthy
            else -> Hazardous
        },
        no2Level = when (airQualityNo2) {
            in 0.0..54.0 -> Good
            in 54.0..100.0 -> Moderate
            in 100.0..360.0 -> UnhealthyForSensGroups
            in 360.0..650.0 -> Unhealthy
            in 650.0..1250.0 -> VeryUnhealthy
            else -> Hazardous
        },
        o3Level = when (airQualityO3) {
            in 0.0..55.0 -> Good
            in 55.0..70.0 -> Moderate
            in 70.0..85.0 -> UnhealthyForSensGroups
            in 85.0..100.0 -> Unhealthy
            in 105.0..200.0 -> VeryUnhealthy
            else -> Hazardous
        },
        so2Level = when (airQualitySo2) {
            in 0.0..35.0 -> Good
            in 35.0..75.0 -> Moderate
            in 75.0..185.0 -> UnhealthyForSensGroups
            in 185.0..305.0 -> Unhealthy
            in 305.0..605.0 -> VeryUnhealthy
            else -> Hazardous
        },
        pm25Level = when (airQualityPm25) {
            in 0.0..12.0 -> Good
            in 12.0..35.5 -> Moderate
            in 35.5..55.5 -> UnhealthyForSensGroups
            in 55.5..150.5 -> Unhealthy
            in 150.5..250.5 -> VeryUnhealthy
            else -> Hazardous
        },
        pm10Level = when (airQualityPm10) {
            in 0.0..55.0 -> Good
            in 55.0..155.0 -> Moderate
            in 155.0..255.0 -> UnhealthyForSensGroups
            in 255.0..355.0 -> Unhealthy
            in 355.0..425.0 -> VeryUnhealthy
            else -> Hazardous
        },
    )
}