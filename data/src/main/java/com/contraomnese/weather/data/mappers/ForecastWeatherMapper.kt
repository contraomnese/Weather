package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.CurrentWeatherNetwork
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
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastInfo
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
            temperature = current.toTemperatureDomain(appSettings.temperatureUnit),
            feelsLike = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> current.feelsLikeC.roundToInt().toString()
                TemperatureUnit.Fahrenheit -> current.feelsLikeF.roundToInt().toString()
            },
            isDay = current.isDay == IS_DAY,
            conditionCode = current.condition.code,
            conditionText = current.condition.text,
            windSpeed = current.toWindDomain(appSettings.windSpeedUnit),
            gustSpeed = current.toGustDomain(appSettings.windSpeedUnit),
            windDirection = current.windDir.translateDirection(),
            windDegree = current.windDegree,
            pressure = current.toPressureDomain(appSettings.pressureUnit),
            isRainingExpected = forecast.forecastDay.first().day.dailyWillItRain == DAILY_WILL_RAIN,
            rainfallLast24Hours = forecast.forecastDay[0].hour.sumOf { hour ->
                if (hour.timeEpoch < location.localtimeEpoch) hour.toPrecipitationDomain(
                    appSettings.precipitationUnit
                ) else DEFAULT_RAINFALL
            },
            rainfallNext24Hours = forecast.forecastDay[0].hour.sumOf { hour ->
                if (hour.timeEpoch >= location.localtimeEpoch) hour.toPrecipitationDomain(
                    appSettings.precipitationUnit
                ) else DEFAULT_RAINFALL
            } +
                    forecast.forecastDay[1].hour.sumOf { hour ->
                        if (hour.timeEpoch < nextDayHourLimit) hour.toPrecipitationDomain(
                            appSettings.precipitationUnit
                        ) else DEFAULT_RAINFALL
                    },
            rainfallNextHour = current.toPrecipitationDomain(appSettings.precipitationUnit),
            humidity = current.humidity,
            dewPoint = current.toDewPoint(appSettings.temperatureUnit),
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

private fun CurrentWeatherNetwork.toTemperatureDomain(temperatureUnit: TemperatureUnit): String {
    return when (temperatureUnit) {
        TemperatureUnit.Celsius -> this.feelsLikeC.roundToInt().toString()
        TemperatureUnit.Fahrenheit -> this.feelsLikeF.roundToInt().toString()
    }
}

private fun CurrentWeatherNetwork.toWindDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.windKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.windMph.roundToInt().toString()
        WindSpeedUnit.Ms -> this.windKph.toMs().roundToInt().toString()
    }
}

private fun CurrentWeatherNetwork.toGustDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.gustKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.gustMph.roundToInt().toString()
        WindSpeedUnit.Ms -> this.gustKph.toMs().roundToInt().toString()
    }
}

private fun CurrentWeatherNetwork.toPressureDomain(pressureUnit: PressureUnit): Int {
    return when (pressureUnit) {
        PressureUnit.MmHg -> (this.pressureIn * MM_IN_INCH).roundToInt()
        PressureUnit.GPa -> this.pressureMb.roundToInt()
    }
}

private fun CurrentWeatherNetwork.toDewPoint(temperatureUnit: TemperatureUnit): Int {
    return when (temperatureUnit) {
        TemperatureUnit.Celsius -> this.dewPointC.roundToInt()
        TemperatureUnit.Fahrenheit -> this.dewPointF.roundToInt()
    }
}

private fun CurrentWeatherNetwork.toPrecipitationDomain(precipitationUnit: PrecipitationUnit): Double {
    return when (precipitationUnit) {
        PrecipitationUnit.Millimeters -> this.precipMm
        PrecipitationUnit.Inches -> this.precipIn
    }
}

private fun HourNetwork.toPrecipitationDomain(precipitationUnit: PrecipitationUnit): Double {
    return when (precipitationUnit) {
        PrecipitationUnit.Millimeters -> this.precipMm
        PrecipitationUnit.Inches -> this.precipIn
    }
}



