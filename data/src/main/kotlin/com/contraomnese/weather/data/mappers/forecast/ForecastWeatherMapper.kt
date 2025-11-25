package com.contraomnese.weather.data.mappers.forecast

import com.contraomnese.weather.data.mappers.UniDirectMapper
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
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastData
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsWeather
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastLocation
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeather
import com.contraomnese.weather.domain.weatherByLocation.model.UvIndex
import com.contraomnese.weather.domain.weatherByLocation.model.Weather
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

private const val HOURS = 24
private const val MINUTES = 60
private const val SECONDS = 60
private const val IS_DAY = 1
private const val IS_SUN_UP = 1
private const val DAILY_WILL_RAIN = 1
private const val DEFAULT_RAINFALL = 0.0

class ForecastWeatherMapper : UniDirectMapper<ForecastData, AppSettings, Forecast> {
    override fun toDomain(entity: ForecastData, appSettings: AppSettings): Forecast {
        val nextDayHourLimit = entity.location.localtimeEpoch + HOURS * MINUTES * SECONDS
        val forecastHours =
            mutableListOf(entity.dailyForecast.first().hour.last { it.timeEpoch <= entity.location.localtimeEpoch })
        forecastHours.addAll(entity.dailyForecast.first().hour.filter { it.timeEpoch > entity.location.localtimeEpoch })
        forecastHours.addAll(entity.dailyForecast.drop(1).first().hour.filter { it.timeEpoch <= nextDayHourLimit })

        val rainfallBeforeNow = entity.dailyForecast[0].hour.filter { hour -> hour.timeEpoch < entity.location.localtimeEpoch }
            .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

        val rainfallAfterNow = entity.dailyForecast[0].hour.filter { hour -> hour.timeEpoch > entity.location.localtimeEpoch }
            .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) } +
                entity.dailyForecast[1].hour.filter { hour -> hour.timeEpoch < nextDayHourLimit }
                    .map { it.toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit) }

        val rainfallNow =
            (entity.dailyForecast[0].hour.firstOrNull { hour -> hour.timeEpoch > entity.location.localtimeEpoch }
                ?: entity.dailyForecast[1].hour.first())
                .toPrecipitationDomain(precipitationUnit = appSettings.precipitationUnit)

        val locationTime = DateTimeParser.parseIso(entity.location.localtime)

        return Forecast(
            location = ForecastLocation(
                city = entity.location.name,
                country = entity.location.country,
                latitude = entity.location.latitude,
                longitude = entity.location.longitude,
                localTimeEpoch = entity.location.localtimeEpoch,
                localTime = locationTime,
                timeZone = TimeZone.of(entity.location.timeZoneId),
                isSunUp = entity.dailyForecast.first().astro.isSunUp == IS_SUN_UP
            ),
            today = Weather(
                temperature = entity.todayForecast.toTemperatureDomain(appSettings.temperatureUnit),
                feelsLikeTemperature = when (appSettings.temperatureUnit) {
                    TemperatureUnit.Celsius -> entity.todayForecast.feelsLikeC.roundToInt().toString()
                    TemperatureUnit.Fahrenheit -> entity.todayForecast.feelsLikeF.roundToInt().toString()
                },
                isDay = entity.todayForecast.isDay == IS_DAY,
                condition = CompactWeatherCondition.fromConditionCode(entity.todayForecast.conditionCode),
                conditionText = entity.todayForecast.conditionText,
                windSpeed = entity.todayForecast.toWindDomain(appSettings.windSpeedUnit),
                gustSpeed = entity.todayForecast.toGustDomain(appSettings.windSpeedUnit),
                windDirection = entity.todayForecast.windDir.translateDirection(),
                windDegree = entity.todayForecast.windDegree,
                pressure = entity.todayForecast.toPressureDomain(appSettings.pressureUnit),
                isRainingExpected = entity.dailyForecast.first().day.dayWillItRain == DAILY_WILL_RAIN,
                rainfallBeforeNow = rainfallBeforeNow,
                rainfallAfterNow = rainfallAfterNow,
                rainfallNow = rainfallNow,
                maxRainfall = rainfallBeforeNow.plus(rainfallNow).plus(rainfallAfterNow).maxOrNull() ?: DEFAULT_RAINFALL,
                humidity = entity.todayForecast.humidity,
                dewPoint = entity.todayForecast.toDewPoint(appSettings.temperatureUnit),
                uvIndex = UvIndex(entity.todayForecast.uv.roundToInt()),
                airQuality = entity.todayForecast.toAirQualityInfo()
            ),
            forecast = ForecastWeather(
                today = entity.dailyForecast.first().toForecastTodayDomain(appSettings),
                days = entity.dailyForecast.map { it.toForecastDayDomain(appSettings) },
                hours = forecastHours.map { it.toDomain(appSettings) }
            ),
            alerts = AlertsWeather(
                alerts = entity.alerts.filter { it.desc.isNotEmpty() }.map { it.desc.replaceFirstChar { char -> char.uppercaseChar() } }
            )
        )
    }
}




