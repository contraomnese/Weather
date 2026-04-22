package com.contraomnese.weather.data.mappers.forecast

import com.contraomnese.weather.data.mappers.forecast.weatherapi.toAirQualityUKIndex
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toAirQualityUSAIndex
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toDewPoint
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toForecastDayDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toForecastTodayDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toGustDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toPrecipitationDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toPressureDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toTemperatureDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toWindDomain
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

private const val HOURS = 24
private const val MINUTES = 60
private const val SECONDS = 60
private const val IS_DAY = 1
private const val IS_SUN_UP = 1
private const val DAILY_WILL_RAIN = 1
private const val DEFAULT_RAINFALL = 0.0

fun ForecastData.toDomain(appSettings: AppSettings): Forecast {

    val timeZone = TimeZone.of(location.timeZoneId)
    val now = Clock.System.now().toLocalDateTime(timeZone).toInstant(timeZone).epochSeconds
    val nextDaySeconds = now + HOURS * MINUTES * SECONDS

    val forecastHours =
        mutableListOf(dailyForecast.first().hour.last { it.timeEpoch <= now })
    forecastHours.addAll(dailyForecast.first().hour.filter { it.timeEpoch > now })
    forecastHours.addAll(dailyForecast[1].hour.filter { it.timeEpoch <= nextDaySeconds })

    val rainfallBeforeNow = dailyForecast[0].hour.filter { hour -> hour.timeEpoch < now }
        .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

    val rainfallAfterNow = dailyForecast[0].hour.filter { hour -> hour.timeEpoch > now }
        .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) } +
            dailyForecast[1].hour.filter { hour -> hour.timeEpoch < nextDaySeconds }
                .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

    val rainfallNow =
        (dailyForecast[0].hour.firstOrNull { hour -> hour.timeEpoch > now }
            ?: dailyForecast[1].hour.first())
            .toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit)

    return Forecast(
        location = ForecastLocation(
            id = location.locationId,
            name = location.name,
            city = location.city,
            country = location.country,
            latitude = location.latitude,
            longitude = location.longitude,
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
            condition = WeatherCondition.fromWeatherApi(todayForecast.conditionCode),
            windSpeed = todayForecast.toWindDomain(appSettings.windSpeedUnit),
            gustSpeed = todayForecast.toGustDomain(appSettings.windSpeedUnit),
            windDegree = todayForecast.windDegree,
            pressure = todayForecast.toPressureDomain(appSettings.pressureUnit),
            willRain = dailyForecast.first().day.dayWillItRain == DAILY_WILL_RAIN,
            rainfallBeforeNow = rainfallBeforeNow,
            rainfallAfterNow = rainfallAfterNow,
            rainfallNow = rainfallNow,
            maxRainfall = rainfallBeforeNow.plus(rainfallNow).plus(rainfallAfterNow).maxOrNull() ?: DEFAULT_RAINFALL,
            humidity = todayForecast.humidity,
            dewPoint = todayForecast.toDewPoint(appSettings.temperatureUnit),
            uvIndex = UvIndex(todayForecast.uv.roundToInt()),
            airQuality = todayForecast.airQualityUKIndex?.let {
                todayForecast.toAirQualityUKIndex()
            } ?: todayForecast.toAirQualityUSAIndex()
        ),
        forecast = ForecastWeather(
            today = dailyForecast.first().toForecastTodayDomain(appSettings),
            days = dailyForecast.map { it.toForecastDayDomain(appSettings, timeZone) },
            hours = forecastHours.map { it.toDomain(appSettings, timeZone) }
        ),
        alerts = AlertsWeather(
            alerts = alerts.filter { it.desc.isNotEmpty() }.map { it.desc.replaceFirstChar { char -> char.uppercaseChar() } }
        )
    )
}




