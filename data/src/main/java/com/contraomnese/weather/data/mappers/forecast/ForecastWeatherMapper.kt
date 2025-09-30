package com.contraomnese.weather.data.mappers.forecast

import com.contraomnese.weather.data.mappers.forecast.internal.toAirQualityInfo
import com.contraomnese.weather.data.mappers.forecast.internal.toDewPoint
import com.contraomnese.weather.data.mappers.forecast.internal.toDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toForecastDayDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toForecastTodayDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toGustDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toPrecipitationDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toPressureDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toTemperatureDomain
import com.contraomnese.weather.data.mappers.forecast.internal.toWindDomain
import com.contraomnese.weather.data.mappers.forecast.internal.translateDirection
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.data.storage.db.forecast.dao.LocationWithForecasts
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsInfo
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfo
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.internal.UvIndex
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

private const val HOURS = 24
private const val MINUTES = 60
private const val SECONDS = 60
private const val IS_DAY = 1
private const val IS_SUN_UP = 1
private const val DAILY_WILL_RAIN = 1
private const val DEFAULT_RAINFALL = 0.0

fun LocationWithForecasts.toDomain(appSettings: AppSettings): ForecastWeatherDomainModel {

    val nextDayHourLimit = location.localtimeEpoch + HOURS * MINUTES * SECONDS
    val forecastHours =
        mutableListOf(forecastDays.first().hour.last { it.timeEpoch <= location.localtimeEpoch })
    forecastHours.addAll(forecastDays.first().hour.filter { it.timeEpoch > location.localtimeEpoch })
    forecastHours.addAll(forecastDays.drop(1).first().hour.filter { it.timeEpoch <= nextDayHourLimit })

    val rainfallBeforeNow = forecastDays[0].hour.filter { hour -> hour.timeEpoch < location.localtimeEpoch }
        .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

    val rainfallAfterNow = forecastDays[0].hour.filter { hour -> hour.timeEpoch > location.localtimeEpoch }
        .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) } +
            forecastDays[1].hour.filter { hour -> hour.timeEpoch < nextDayHourLimit }
                .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

    val rainfallNow =
        (forecastDays[0].hour.firstOrNull { hour -> hour.timeEpoch > location.localtimeEpoch } ?: forecastDays[1].hour.first())
        .toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit)

    val locationTime = DateTimeParser.parseIso(location.localtime)

    return ForecastWeatherDomainModel(
        locationInfo = LocationInfo(
            name = location.name,
            country = location.country,
            latitude = location.latitude,
            longitude = location.longitude,
            localTimeEpoch = location.localtimeEpoch,
            localTime = locationTime,
            timeZone = TimeZone.of(location.timeZoneId),
            isSunUp = forecastDays.first().astro.isSunUp == IS_SUN_UP
        ),
        currentInfo = CurrentInfo(
            temperature = forecastCurrent.toTemperatureDomain(appSettings.temperatureUnit),
            feelsLikeTemperature = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> forecastCurrent.feelsLikeC.roundToInt().toString()
                TemperatureUnit.Fahrenheit -> forecastCurrent.feelsLikeF.roundToInt().toString()
            },
            isDay = forecastCurrent.isDay == IS_DAY,
            condition = CompactWeatherCondition.fromConditionCode(forecastCurrent.conditionCode),
            conditionText = forecastCurrent.conditionText,
            windSpeed = forecastCurrent.toWindDomain(appSettings.windSpeedUnit),
            gustSpeed = forecastCurrent.toGustDomain(appSettings.windSpeedUnit),
            windDirection = forecastCurrent.windDir.translateDirection(),
            windDegree = forecastCurrent.windDegree,
            pressure = forecastCurrent.toPressureDomain(appSettings.pressureUnit),
            isRainingExpected = forecastDays.first().day.dayWillItRain == DAILY_WILL_RAIN,
            rainfallBeforeNow = rainfallBeforeNow.toImmutableList(),
            rainfallAfterNow = rainfallAfterNow.toImmutableList(),
            rainfallNow = rainfallNow,
            maxRainfall = rainfallBeforeNow.plus(rainfallNow).plus(rainfallAfterNow).maxOrNull() ?: DEFAULT_RAINFALL,
            humidity = forecastCurrent.humidity,
            dewPoint = forecastCurrent.toDewPoint(appSettings.temperatureUnit),
            uvIndex = UvIndex(forecastCurrent.uv.roundToInt()),
            airQuality = forecastCurrent.toAirQualityInfo()
        ),
        forecastInfo = ForecastInfo(
            today = forecastDays.first().toForecastTodayDomain(appSettings),
            forecastDays = forecastDays.map { it.toForecastDayDomain(appSettings) }.toPersistentList(),
            forecastHours = forecastHours.map { it.toDomain(appSettings) }.toPersistentList()
        ),
        alertsInfo = AlertsInfo(
            alerts = forecastAlert.map { it.desc }.toPersistentList()
        )
    )
}



