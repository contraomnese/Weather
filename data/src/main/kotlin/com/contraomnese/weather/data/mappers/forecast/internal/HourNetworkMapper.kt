package com.contraomnese.weather.data.mappers.forecast.internal

import com.contraomnese.weather.data.network.models.weatherapi.HourNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

private const val IS_DAY = 1

internal fun ForecastHourEntity.toDomain(appSettings: AppSettings): ForecastHour {

    val instant = Instant.fromEpochSeconds(timeEpoch)
    val localDateTime = instant.toLocalDateTime(TimeZone.of(appSettings.timezone))
    val locationDateTime = LocationDateTime(localDateTime)

    return ForecastHour(
        temperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> tempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> tempF.roundToInt().toString()
        },
        condition = WeatherCondition.fromWeatherApi(conditionCode),
        time = locationDateTime.toLocalTime(),
        isDay = isDay == IS_DAY
    )
}

internal fun HourNetwork.toEntity(forecastDayId: Int) = ForecastHourEntity(
    forecastDailyId = forecastDayId,
    timeEpoch = timeEpoch,
    tempC = tempC,
    tempF = tempF,
    isDay = isDay,
    conditionCode = condition.code,
    windMph = windMph,
    windKph = windKph,
    windDegree = windDegree,
    pressureMb = pressureMb,
    pressureIn = pressureIn,
    precipMm = precipMm,
    precipIn = precipIn,
    snowCm = snowCm,
    humidity = humidity,
    cloud = cloud,
    feelsLikeC = feelsLikeC,
    feelsLikeF = feelsLikeF,
    dewPointC = dewPointC,
    dewPointF = dewPointF,
    chanceOfRain = chanceOfRain,
    chanceOfSnow = chanceOfSnow,
    visibilityKm = visibilityKm,
    visibilityMiles = visibilityMiles,
    gustMph = gustMph,
    gustKph = gustKph,
    uv = uv,
)