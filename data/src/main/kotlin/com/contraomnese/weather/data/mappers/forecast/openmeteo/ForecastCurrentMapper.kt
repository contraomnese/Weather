package com.contraomnese.weather.data.mappers.forecast.openmeteo

import com.contraomnese.weather.data.mappers.utils.celsiusToFahrenheit
import com.contraomnese.weather.data.mappers.utils.hPaToInchesHg
import com.contraomnese.weather.data.mappers.utils.kmToMiles
import com.contraomnese.weather.data.mappers.utils.kphToMph
import com.contraomnese.weather.data.mappers.utils.mmToInch
import com.contraomnese.weather.data.network.models.openweather.airquality.AirQualityNetwork
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastCurrentNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity

internal fun ForecastCurrentNetwork.toEntity(
    forecastLocationId: Int,
    visibility: Double,
    dewPoint: Double,
    airQualityCurrent: AirQualityNetwork,
    updatedTime: Long,
) = ForecastTodayEntity(
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
    uv = airQualityCurrent.hourly.uvIndex.max(),
    gustKph = windGusts10m,
    gustMph = kphToMph(windGusts10m),
    airQualityCo = airQualityCurrent.current.carbonMonoxide,
    airQualityNo2 = airQualityCurrent.current.nitrogenDioxide,
    airQualityO3 = airQualityCurrent.current.ozone,
    airQualitySo2 = airQualityCurrent.current.sulphurDioxide,
    airQualityPm10 = airQualityCurrent.current.pm10,
    airQualityPm25 = airQualityCurrent.current.pm25,
    airQualityUsEpaIndex = airQualityCurrent.current.usAqi,
    airQualityGbDefraIndex = 0,
    lastUpdatedEpoch = updatedTime
)

