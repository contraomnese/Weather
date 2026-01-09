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
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastData
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsWeather
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastLocation
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeather
import com.contraomnese.weather.domain.weatherByLocation.model.UvIndex
import com.contraomnese.weather.domain.weatherByLocation.model.Weather
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

private const val HOURS = 24
private const val MINUTES = 60
private const val SECONDS = 60
private const val IS_DAY = 1
private const val IS_SUN_UP = 1
private const val DAILY_WILL_RAIN = 1
private const val DEFAULT_RAINFALL = 0.0

fun ForecastData.toDomain(appSettings: AppSettings): Forecast {
    val nextDayHourLimit = location.localtimeEpoch + HOURS * MINUTES * SECONDS
    val forecastHours =
        mutableListOf(dailyForecast.first().hour.last { it.timeEpoch <= location.localtimeEpoch })
    forecastHours.addAll(dailyForecast.first().hour.filter { it.timeEpoch > location.localtimeEpoch })
    forecastHours.addAll(dailyForecast.drop(1).first().hour.filter { it.timeEpoch <= nextDayHourLimit })

    val rainfallBeforeNow = dailyForecast[0].hour.filter { hour -> hour.timeEpoch < location.localtimeEpoch }
        .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

    val rainfallAfterNow = dailyForecast[0].hour.filter { hour -> hour.timeEpoch > location.localtimeEpoch }
        .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) } +
            dailyForecast[1].hour.filter { hour -> hour.timeEpoch < nextDayHourLimit }
                .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

    val rainfallNow =
        (dailyForecast[0].hour.firstOrNull { hour -> hour.timeEpoch > location.localtimeEpoch }
            ?: dailyForecast[1].hour.first())
            .toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit)

    val locationTime = DateTimeParser.parseIso(location.localtime)

    return Forecast(
        location = ForecastLocation(
            id = location.locationId,
            city = location.name,
            country = location.country,
            latitude = location.latitude,
            longitude = location.longitude,
            localTimeEpoch = location.localtimeEpoch,
            localTime = locationTime,
            timeZone = TimeZone.of(location.timeZoneId),
            isSunUp = dailyForecast.first().astro.isSunUp == IS_SUN_UP
        ),
        today = Weather(
            temperature = todayForecast.toTemperatureDomain(appSettings.temperatureUnit),
            feelsLikeTemperature = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> todayForecast.feelsLikeC.roundToInt().toString()
                TemperatureUnit.Fahrenheit -> todayForecast.feelsLikeF.roundToInt().toString()
            },
            isDay = todayForecast.isDay == IS_DAY,
            condition = WeatherCondition.fromConditionCode(todayForecast.conditionCode),
            conditionText = todayForecast.conditionText,
            windSpeed = todayForecast.toWindDomain(appSettings.windSpeedUnit),
            gustSpeed = todayForecast.toGustDomain(appSettings.windSpeedUnit),
            windDirection = todayForecast.windDir.translateDirection(),
            windDegree = todayForecast.windDegree,
            pressure = todayForecast.toPressureDomain(appSettings.pressureUnit),
            isRainingExpected = dailyForecast.first().day.dayWillItRain == DAILY_WILL_RAIN,
            rainfallBeforeNow = rainfallBeforeNow,
            rainfallAfterNow = rainfallAfterNow,
            rainfallNow = rainfallNow,
            maxRainfall = rainfallBeforeNow.plus(rainfallNow).plus(rainfallAfterNow).maxOrNull() ?: DEFAULT_RAINFALL,
            humidity = todayForecast.humidity,
            dewPoint = todayForecast.toDewPoint(appSettings.temperatureUnit),
            uvIndex = UvIndex(todayForecast.uv.roundToInt()),
            airQuality = todayForecast.toAirQualityInfo()
        ),
        forecast = ForecastWeather(
            today = dailyForecast.first().toForecastTodayDomain(appSettings),
            days = dailyForecast.map { it.toForecastDayDomain(appSettings) },
            hours = forecastHours.map { it.toDomain(appSettings) }
        ),
        alerts = AlertsWeather(
            alerts = alerts.filter { it.desc.isNotEmpty() }.map { it.desc.replaceFirstChar { char -> char.uppercaseChar() } }
        )
    )
}




