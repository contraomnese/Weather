package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
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

fun ForecastWeatherResponse.toDomain(appSettings: AppSettings): ForecastWeatherDomainModel {

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
            temperature = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> current.tempC.roundToInt().toString()
                TemperatureUnit.Fahrenheit -> current.tempF.roundToInt().toString()
            },
            feelsLike = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> current.feelsLikeC.roundToInt().toString()
                TemperatureUnit.Fahrenheit -> current.feelsLikeF.roundToInt().toString()
            },
            isDay = current.isDay == IS_DAY,
            conditionCode = current.condition.code,
            conditionText = current.condition.text,
            windSpeed = when (appSettings.windSpeedUnit) {
                WindSpeedUnit.Kph -> current.windKph.roundToInt().toString()
                WindSpeedUnit.Mph -> current.windMph.roundToInt().toString()
                WindSpeedUnit.Ms -> current.windKph.toMs().roundToInt().toString()
            },
            gustSpeed = when (appSettings.windSpeedUnit) {
                WindSpeedUnit.Kph -> current.gustKph.roundToInt().toString()
                WindSpeedUnit.Mph -> current.gustMph.roundToInt().toString()
                WindSpeedUnit.Ms -> current.gustKph.toMs().roundToInt().toString()
            },
            windDirection = current.windDir.translateDirection(),
            windDegree = current.windDegree,
            pressure = when (appSettings.pressureUnit) {
                PressureUnit.MmHg -> (current.pressureIn * MM_IN_INCH).roundToInt()
                PressureUnit.InchHg -> current.pressureIn.roundToInt()
            },
            isRainingExpected = forecast.forecastDay.first().day.dailyWillItRain == DAILY_WILL_RAIN,
            rainfallLast24Hours = forecast.forecastDay[0].hour.sumOf { hour -> if (hour.timeEpoch < location.localtimeEpoch) hour.precipMm else DEFAULT_RAINFALL },
            rainfallNext24Hours = forecast.forecastDay[0].hour.sumOf { hour -> if (hour.timeEpoch >= location.localtimeEpoch) hour.precipMm else DEFAULT_RAINFALL } +
                    forecast.forecastDay[1].hour.sumOf { hour -> if (hour.timeEpoch < nextDayHourLimit) hour.precipMm else DEFAULT_RAINFALL },
            rainfallNextHour = when (appSettings.precipitationUnit) {
                PrecipitationUnit.Millimeters -> current.precipMm
                PrecipitationUnit.Inches -> current.precipIn
            },
            humidity = current.humidity,
            dewPoint = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> current.dewPointC
                TemperatureUnit.Fahrenheit -> current.dewPointF
            },
            uvIndex = UvIndex(current.uv.roundToInt()),
            airQualityIndex = current.airQuality.toDomain()
        ),
        forecastInfo = ForecastInfo(
            today = forecast.forecastDay.first().toForecastTodayDomain(appSettings),
            forecastDays = forecast.forecastDay.map { it.toForecastDayDomain(appSettings) }.toPersistentList(),
            forecastHours = forecastHours.map { it.toDomain(appSettings) }.toPersistentList()
        ),
        alertsInfo = AlertsInfo(
            alerts = alerts.alert.map { it.desc }.toPersistentList()
        )
    )
}

fun ForecastDayNetwork.toForecastTodayDomain(appSettings: AppSettings): ForecastToday {

    return ForecastToday(
        maxTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.maxTempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> day.maxTempF.roundToInt().toString()
        },
        minTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.minTempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> day.minTempF.roundToInt().toString()
        },
        conditionCode = day.condition.code,
        conditionText = day.condition.text,
        totalUvIndex = day.uv.toString(),
        rainChance = day.dailyChanceOfRain.toString(),
        totalRainFull = when (appSettings.precipitationUnit) {
            PrecipitationUnit.Millimeters -> day.totalPrecipMm.roundToInt().toString()
            PrecipitationUnit.Inches -> day.totalPrecipIn.roundToInt().toString()
        },
        sunrise = DateTimeParser.parseAmPmTime(astro.sunrise),
        sunset = DateTimeParser.parseAmPmTime(astro.sunset),
    )
}

fun ForecastDayNetwork.toForecastDayDomain(appSettings: AppSettings): ForecastDay {
    return ForecastDay(
        dayName = getDayOfWeek(dateEpoch),
        maxTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.maxTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.maxTempF.roundToInt()
        },
        minTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.minTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.minTempF.roundToInt()
        },
        conditionCode = day.condition.code,
        totalRainFull = when (appSettings.precipitationUnit) {
            PrecipitationUnit.Millimeters -> day.totalPrecipMm.roundToInt()
            PrecipitationUnit.Inches -> day.totalPrecipIn.roundToInt()
        },
    )
}

fun HourNetwork.toDomain(appSettings: AppSettings): ForecastHour {
    return ForecastHour(
        temperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> tempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> tempF.roundToInt().toString()
        },
        conditionCode = condition.code,
        time = time.split(" ")[1],
        isDay = isDay == IS_DAY
    )
}



