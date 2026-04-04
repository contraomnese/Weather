package com.contraomnese.weather.data.mappers.forecast.openmeteo

import com.contraomnese.weather.data.mappers.utils.celsiusToFahrenheit
import com.contraomnese.weather.data.mappers.utils.hPaToInchesHg
import com.contraomnese.weather.data.mappers.utils.kmToMiles
import com.contraomnese.weather.data.mappers.utils.kphToMph
import com.contraomnese.weather.data.mappers.utils.mmToInch
import com.contraomnese.weather.data.network.models.openweather.airquality.AirQualityNetwork
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastHourly
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import kotlin.time.Duration.Companion.days

fun ForecastHourEntity.takeForecastHourByTime(forecastDaily: ForecastDailyEntity) =
    this.timeEpoch < forecastDaily.dateEpoch + 1.days.inWholeSeconds && this.timeEpoch >= forecastDaily.dateEpoch

fun ForecastHourly.toForecastHourlyEntities(airQuality: AirQualityNetwork): List<ForecastHourEntity> =
    time.indices.map { index ->
        ForecastHourEntity(
            forecastDailyId = index,
            timeEpoch = time[index],
            tempC = temperature2m[index],
            tempF = celsiusToFahrenheit(temperature2m[index]),
            isDay = isDay[index],
            conditionCode = weatherCode[index],
            windKph = windSpeed10m[index],
            windMph = kphToMph(windSpeed10m[index]),
            windDegree = windDirection10m[index],
            pressureMb = surfacePressure[index],
            pressureIn = hPaToInchesHg(surfacePressure[index]),
            precipMm = precipitation[index],
            precipIn = mmToInch(precipitation[index]),
            snowCm = snowfall[index],
            humidity = relativeHumidity2m[index],
            cloud = cloudCover[index],
            feelsLikeC = apparentTemperature[index],
            feelsLikeF = celsiusToFahrenheit(apparentTemperature[index]),
            dewPointC = dewPoint2m[index],
            dewPointF = celsiusToFahrenheit(dewPoint2m[index]),
            chanceOfRain = precipitationProbability[index],
            chanceOfSnow = precipitationProbability[index],
            visibilityKm = visibility[index],
            visibilityMiles = kmToMiles(visibility[index]),
            gustKph = windGusts10m[index],
            gustMph = kphToMph(windGusts10m[index]),
            uv = airQuality.hourly.uvIndex[index]
        )
    }