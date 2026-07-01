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
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastLocation
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeather
import com.contraomnese.weather.domain.weatherByLocation.model.LocationTime
import com.contraomnese.weather.domain.weatherByLocation.model.TodayForecast
import com.contraomnese.weather.domain.weatherByLocation.model.UvIndex
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
    val nowEpoch = Clock.System.now().toLocalDateTime(timeZone).toInstant(timeZone).epochSeconds
    val after24HoursEpoch = nowEpoch + HOURS * MINUTES * SECONDS

    val todayEntity = dailyForecast[0]
    val nextDayEntity = dailyForecast[1]

    val nowHour = todayEntity.hour.last { it.timeEpoch <= nowEpoch }
    val hoursToNextDay = todayEntity.hour.filter { it.timeEpoch > nowEpoch }
    val hoursToNowInNextDay = nextDayEntity.hour.filter { it.timeEpoch <= after24HoursEpoch }

    val forecastHoursEntity = listOf(nowHour) + hoursToNextDay + hoursToNowInNextDay

    val forecastHours = forecastHoursEntity.map { it.toDomain(appSettings, timeZone) }.toMutableList()

    val firstTime = forecastHoursEntity.firstOrNull()?.timeEpoch ?: 0
    val lastTime = forecastHoursEntity.lastOrNull()?.timeEpoch ?: 0

    val astroEvents = listOf(
        todayEntity.astro.sunrise to WeatherCondition.SUNRISE,
        todayEntity.astro.sunset to WeatherCondition.SUNSET,
        nextDayEntity.astro.sunrise to WeatherCondition.SUNRISE,
        nextDayEntity.astro.sunset to WeatherCondition.SUNSET
    )

    astroEvents.forEach { (timeEpoch, condition) ->
        if (timeEpoch in firstTime..lastTime) {
            forecastHours.add(
                index = forecastHoursEntity.indexOfFirst { it.timeEpoch > timeEpoch },
                ForecastHour(
                    time = LocationTime.fromEpochSeconds(timeEpoch, timeZone),
                    condition = condition,
                )
            )
        }
    }

    val precipUnit = appSettings.precipitationUnit

    val rainfallBeforeNow = todayEntity.hour
        .filter { hour -> hour.timeEpoch < nowEpoch }
        .map { it.toPrecipitationDomain(precipitationUnit = precipUnit) }

    val rainfallAfterNow = (todayEntity.hour.filter { it.timeEpoch > nowEpoch } +
            nextDayEntity.hour.filter { it.timeEpoch < after24HoursEpoch })
        .map { it.toPrecipitationDomain(precipUnit) }

    val rainfallNow = (todayEntity.hour.firstOrNull { it.timeEpoch > nowEpoch } ?: nextDayEntity.hour.first())
        .toPrecipitationDomain(precipUnit)

    val allRainfall = rainfallBeforeNow + rainfallNow + rainfallAfterNow

    return Forecast(
        location = ForecastLocation(
            id = location.locationId,
            name = location.name,
            city = location.city,
            country = location.country,
            latitude = location.latitude,
            longitude = location.longitude,
            timeZone = timeZone,
            isSunUp = dailyForecast.first().astro.isSunUp == IS_SUN_UP
        ),
        today = TodayForecast(
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
            maxRainfall = allRainfall.maxOrNull() ?: DEFAULT_RAINFALL,
            humidity = todayForecast.humidity,
            dewPoint = todayForecast.toDewPoint(appSettings.temperatureUnit),
            uvIndex = UvIndex(todayForecast.uv.roundToInt()),
            airQuality = todayForecast.airQualityUKIndex?.let {
                todayForecast.toAirQualityUKIndex()
            } ?: todayForecast.toAirQualityUSAIndex()
        ),
        forecast = ForecastWeather(
            today = todayEntity.toForecastTodayDomain(appSettings, timeZone),
            days = dailyForecast.map { it.toForecastDayDomain(appSettings, timeZone) },
            hours = forecastHours
        ),
        alerts = AlertsWeather(
            alerts = alerts.filter { it.desc.isNotEmpty() }
                .map { it.desc.replaceFirstChar { char -> char.uppercaseChar() } }
        ),
        expiresAt = location.lastUpdatedTime + appSettings.favoritesForecastUpdateInterval
    )
}




