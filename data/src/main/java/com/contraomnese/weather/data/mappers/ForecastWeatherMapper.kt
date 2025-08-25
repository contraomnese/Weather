package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.domain.weatherByLocation.model.AirQualityInfo
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsInfo
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfo
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel
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
            airQualityIndex = AirQualityInfo(
                aqiIndex = current.airQuality.gbDefraIndex,
                aqiText = when (current.airQuality.gbDefraIndex) {
                    in 1..3 -> "Low"
                    in 4..6 -> "Moderate"
                    in 7..9 -> "High"
                    else -> "Very High"
                },
                coLevel = when (current.airQuality.co / 1000) {
                    in 0.0..4.5 -> PollutantLevel.Good
                    in 4.5..9.4 -> PollutantLevel.Moderate
                    else -> PollutantLevel.Bad
                },
                no2Level = when (current.airQuality.no2) {
                    in 0.0..100.0 -> PollutantLevel.Good
                    in 100.0..200.0 -> PollutantLevel.Moderate
                    else -> PollutantLevel.Bad
                },
                o3Level = when (current.airQuality.o3) {
                    in 0.0..100.0 -> PollutantLevel.Good
                    in 100.0..160.0 -> PollutantLevel.Moderate
                    else -> PollutantLevel.Bad
                },
                so2Level = when (current.airQuality.so2) {
                    in 0.0..35.0 -> PollutantLevel.Good
                    in 35.0..75.0 -> PollutantLevel.Moderate
                    else -> PollutantLevel.Bad
                },
                pm25Level = when (current.airQuality.pm25) {
                    in 0.0..12.0 -> PollutantLevel.Good
                    in 12.0..35.4 -> PollutantLevel.Moderate
                    else -> PollutantLevel.Bad
                },
                pm10Level = when (current.airQuality.pm10) {
                    in 0.0..55.0 -> PollutantLevel.Good
                    in 55.0..154.0 -> PollutantLevel.Moderate
                    else -> PollutantLevel.Bad
                },
            )
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
        isDay = isDay == 1
    )
}

fun getDayOfWeek(epochSeconds: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = Instant.fromEpochSeconds(epochSeconds)
    val localDate = instant.toLocalDateTime(timeZone).date
    return localDate.dayOfWeek.name.slice(0..2)
}

