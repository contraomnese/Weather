package com.contraomnese.weather.data.mappers.forecast.openmeteo

import com.contraomnese.weather.data.mappers.utils.celsiusToFahrenheit
import com.contraomnese.weather.data.mappers.utils.hPaToInchesHg
import com.contraomnese.weather.data.mappers.utils.kmToMiles
import com.contraomnese.weather.data.mappers.utils.kphToMph
import com.contraomnese.weather.data.mappers.utils.mmToInch
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastCurrentNetwork
import com.contraomnese.weather.data.network.responses.OpenMeteoAirQualityResponse
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import kotlin.time.Duration.Companion.days

internal fun ForecastCurrentNetwork.toEntity(
    forecastLocationId: Int,
    airQualityCurrent: OpenMeteoAirQualityResponse,
    forecastDayEntities: List<ForecastDayEntity>,
    forecastHourlyEntities: List<ForecastHourEntity>,
): ForecastTodayEntity {
    val hoursPerDay = 1.days.inWholeHours.toInt()
    val visibility = forecastDayEntities[0].avgVisKm
    val dewPoint = forecastHourlyEntities
        .take(hoursPerDay)
        .map { it.dewPointC }
        .average()
        .takeUnless { it.isNaN() } ?: 0.0

    return ForecastTodayEntity(
        forecastLocationId = forecastLocationId,
        tempC = temperature2m,
        tempF = celsiusToFahrenheit(temperature2m),
        isDay = isDay,
        conditionCode = weatherCode,
        windMph = kphToMph(windSpeed10m),
        windKph = windSpeed10m,
        windDegree = windDirection10m,
        pressureMb = surfacePressure,
        pressureIn = hPaToInchesHg(surfacePressure),
        precipMm = precipitation,
        precipIn = mmToInch(precipitation),
        humidity = relativeHumidity2m,
        cloud = cloudCover,
        feelsLikeC = apparentTemperature,
        feelsLikeF = celsiusToFahrenheit(apparentTemperature),
        dewPointC = dewPoint,
        dewPointF = celsiusToFahrenheit(dewPoint),
        visibilityKm = visibility,
        visibilityMiles = kmToMiles(visibility),
        uv = forecastHourlyEntities.take(hoursPerDay).maxOf { it.uv },
        gustKph = windGusts10m,
        gustMph = kphToMph(windGusts10m),
        airQualityCo = airQualityCurrent.current.carbonMonoxide,
        airQualityNo2 = airQualityCurrent.current.nitrogenDioxide,
        airQualityO3 = airQualityCurrent.current.ozone,
        airQualitySo2 = airQualityCurrent.current.sulphurDioxide,
        airQualityPm10 = airQualityCurrent.current.pm10,
        airQualityPm25 = airQualityCurrent.current.pm25,
        airQualityUSAIndex = airQualityCurrent.current.usAqi,
        airQualityUKIndex = null,
        lastUpdatedEpoch = time
    )
}

