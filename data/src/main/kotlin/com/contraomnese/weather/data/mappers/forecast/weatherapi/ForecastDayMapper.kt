package com.contraomnese.weather.data.mappers.forecast.weatherapi

import com.contraomnese.weather.data.network.models.weatherapi.ForecastDayNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.locations.dto.DailyForecastData
import com.contraomnese.weather.data.utils.getDayOfWeek
import com.contraomnese.weather.data.utils.getNumberOfMonth
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt

internal fun DailyForecastData.toForecastTodayDomain(appSettings: AppSettings): ForecastToday {

    return ForecastToday(
        maxTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.maxTempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> day.maxTempF.roundToInt().toString()
        },
        minTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.minTempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> day.minTempF.roundToInt().toString()
        },
        conditionCode = day.conditionCode,
        totalUvIndex = day.uv.toString(),
        rainChance = day.dayChanceOfRain.toString(),
        totalRainFull = when (appSettings.precipitationUnit) {
            PrecipitationUnit.Millimeters -> day.totalPrecipMm.roundToInt().toString()
            PrecipitationUnit.Inches -> day.totalPrecipIn.roundToInt().toString()
        },
        sunrise = DateTimeParser.parseAmPmTime(astro.sunrise),
        sunset = DateTimeParser.parseAmPmTime(astro.sunset),
    )
}

internal fun DailyForecastData.toForecastDayDomain(appSettings: AppSettings, timeZone: TimeZone): ForecastDay {
    return ForecastDay(
        dayNumber = getNumberOfMonth(forecast.dateEpoch, timeZone),
        dayName = getDayOfWeek(forecast.dateEpoch, timeZone),
        maxTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.maxTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.maxTempF.roundToInt()
        },
        minTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.minTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.minTempF.roundToInt()
        },
        condition = WeatherCondition.fromWeatherApi(day.conditionCode),
        totalRainFull = when (appSettings.precipitationUnit) {
            PrecipitationUnit.Millimeters -> day.totalPrecipMm.roundToInt()
            PrecipitationUnit.Inches -> day.totalPrecipIn.roundToInt()
        },
        precipitationProbability = day.dayChanceOfRain.takeIf { it != 0 } ?: day.dayChanceOfSnow
    )
}

internal fun ForecastDayNetwork.toForecastDayEntity(locationId: Int): ForecastDailyEntity {

    return ForecastDailyEntity(
        forecastLocationId = locationId,
        dateEpoch = dateEpoch,
    )
}

internal fun ForecastDayNetwork.toEntity(forecastDayId: Int) = ForecastDayEntity(
    forecastDailyId = forecastDayId,
    maxTempC = day.maxTempC,
    minTempC = day.minTempC,
    maxTempF = day.maxTempF,
    minTempF = day.minTempF,
    avgTempC = day.avgTempC,
    avgTempF = day.avgTempF,
    maxWindMph = day.maxWindMph,
    maxWindKph = day.maxWindKph,
    totalPrecipMm = day.totalPrecipMm,
    totalPrecipIn = day.totalPrecipIn,
    totalSnowCm = day.totalSnowCm,
    avgVisKm = day.avgVisKm,
    avgVisMiles = day.avgVisMiles,
    avgHumidity = day.avgHumidity,
    conditionCode = day.condition.code,
    uv = day.uv,
    dayWillItRain = day.dailyWillItRain,
    dayChanceOfRain = day.dailyChanceOfRain,
    dayWillItSnow = day.dailyWillItSnow,
    dayChanceOfSnow = day.dailyChanceOfSnow,
)