package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsInfo
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfo
import com.contraomnese.weather.domain.weatherByLocation.model.UvIndex
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

private const val HOURS = 24
private const val MINUTES = 60
private const val SECONDS = 60
private const val MM_IN_INCH = 25.4
private const val IS_DAY = 1
private const val DAILY_WILL_RAIN = 1
private const val DEFAULT_RAINFALL = 0.0

fun ForecastWeatherResponse.toDomain(): ForecastWeatherDomainModel {

    val nextDayHourLimit = location.localtimeEpoch + HOURS * MINUTES * SECONDS
    val forecastHours =
        mutableListOf(forecast.forecastDay.first().hour.last { it.timeEpoch <= location.localtimeEpoch })
    forecastHours.addAll(forecast.forecastDay.first().hour.filter { it.timeEpoch > location.localtimeEpoch })
    forecastHours.addAll(forecast.forecastDay.drop(1).first().hour.filter { it.timeEpoch <= nextDayHourLimit })

    val locationTime = DateTimeParser.parseIso(location.localtime)
    val isAfterMidDay = locationTime?.let { locTime ->
        val sunrise = DateTimeParser.parseAmPmTime(forecast.forecastDay.first().astro.sunrise)
        val sunset = DateTimeParser.parseAmPmTime(forecast.forecastDay.first().astro.sunset)
        if (sunrise != null && sunset != null) locTime.toMinutes() >= sunset.toMinutes() - sunrise.toMinutes()
        else null
    }

    return ForecastWeatherDomainModel(
        locationInfo = LocationInfo(
            locationTimeEpoch = location.localtimeEpoch,
            locationTime = DateTimeParser.parseIso(location.localtime),
            isAfterMidDay = isAfterMidDay
        ),
        currentInfo = CurrentInfo(
            temperature = current.tempC.roundToInt().toString(),
            feelsLike = current.feelsLikeC.roundToInt().toString(),
            isDay = current.isDay == IS_DAY,
            conditionCode = current.condition.code,
            conditionText = current.condition.text,
            windSpeed = current.windKph.toMs().roundToInt().toString(),
            gustSpeed = current.gustKph.toMs().roundToInt().toString(),
            windDirection = current.windDir.translateDirection(),
            windDegree = current.windDegree,
            pressure = (current.pressureIn * MM_IN_INCH).roundToInt(),
            isRainingExpected = forecast.forecastDay.first().day.dailyWillItRain == DAILY_WILL_RAIN,
            rainfallLast24Hours = forecast.forecastDay[0].hour.sumOf { hour -> if (hour.timeEpoch < location.localtimeEpoch) hour.precipMm else DEFAULT_RAINFALL },
            rainfallNext24Hours = forecast.forecastDay[0].hour.sumOf { hour -> if (hour.timeEpoch >= location.localtimeEpoch) hour.precipMm else DEFAULT_RAINFALL } +
                    forecast.forecastDay[1].hour.sumOf { hour -> if (hour.timeEpoch < nextDayHourLimit) hour.precipMm else DEFAULT_RAINFALL },
            rainfallNextHour = current.precipMm,
            humidity = current.humidity,
            dewPoint = current.dewPointC.roundToInt(),
            uvIndex = UvIndex(current.uv.roundToInt()),
            airQualityIndex = current.airQuality.toDomain()
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
        sunrise = DateTimeParser.parseAmPmTime(astro.sunrise),
        sunset = DateTimeParser.parseAmPmTime(astro.sunset),
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
        isDay = isDay == IS_DAY
    )
}



