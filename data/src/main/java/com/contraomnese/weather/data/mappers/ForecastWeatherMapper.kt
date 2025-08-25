package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsInfo
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfo
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

fun ForecastWeatherResponse.toDomain(): ForecastWeatherDomainModel {

    val nextDayHourLimit = location.localtimeEpoch + 24 * 60 * 60
    val forecastHours =
        mutableListOf(forecast.forecastDay.first().hour.last { it.timeEpoch <= location.localtimeEpoch })
    forecastHours.addAll(forecast.forecastDay.first().hour.filter { it.timeEpoch > location.localtimeEpoch })
    forecastHours.addAll(forecast.forecastDay.drop(1).first().hour.filter { it.timeEpoch <= nextDayHourLimit })

    return ForecastWeatherDomainModel(
        locationInfo = LocationInfo(
            locationTimeEpoch = location.localtimeEpoch,
            locationTime = location.localtime
        ),
        currentInfo = CurrentInfo(
            temperature = current.tempC.roundToInt().toString(),
            feelsLike = current.feelsLikeC.roundToInt().toString(),
            isDay = current.isDay == 1,
            conditionCode = current.condition.code,
            conditionText = current.condition.text,
            windSpeed = current.windKph.roundToInt().toString(),
            windDirection = current.windDir,
            windDegree = current.windDegree,
            pressure = current.pressureMb.toString(),
            humidity = current.humidity.toString(),
            uvIndex = current.uv.toString(),
            airQualityIndex = current.airQuality.usEpaIndex
        ),
        forecastInfo = ForecastInfo(
            today = forecast.forecastDay.first().toForecastTodayDomain(),
            forecastDays = forecast.forecastDay.map { it.toForecastDayDomain() }.toPersistentList(),
            forecastHours = forecastHours.map { it.toDomain() }.toPersistentList()
        ),
        alertsInfo = AlertsInfo(
            alerts = alerts.alert.map { it.desc }.toPersistentList()
        )
    )
}

fun ForecastDayNetwork.toForecastTodayDomain(): ForecastToday {

    return ForecastToday(
        maxTemperature = day.maxTempC.roundToInt().toString(),
        minTemperature = day.minTempC.roundToInt().toString(),
        conditionCode = day.condition.code,
        conditionText = day.condition.text,
        totalUvIndex = day.uv.toString(),
        rainChance = day.dailyChanceOfRain.toString(),
        totalRainFull = day.totalPrecipMm.roundToInt().toString(),
        sunrise = astro.sunrise,
        sunset = astro.sunset
    )
}

fun ForecastDayNetwork.toForecastDayDomain(): ForecastDay {
    return ForecastDay(
        dayName = getDayOfWeek(dateEpoch),
        maxTemperature = day.maxTempC.roundToInt(),
        minTemperature = day.minTempC.roundToInt(),
        conditionCode = day.condition.code,
        totalRainFull = day.totalPrecipMm.roundToInt(),
    )
}

fun HourNetwork.toDomain(): ForecastHour {
    return ForecastHour(
        temperature = tempC.roundToInt().toString(),
        conditionCode = condition.code,
        time = time.split(" ")[1],
    )
}

fun getDayOfWeek(epochSeconds: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = Instant.fromEpochSeconds(epochSeconds)
    val localDate = instant.toLocalDateTime(timeZone).date
    return localDate.dayOfWeek.name.slice(0..2)
}

