package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastDayWithDetails
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import kotlin.math.roundToInt

fun ForecastDayWithDetails.toForecastTodayDomain(appSettings: AppSettings): ForecastToday {

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
        conditionText = day.conditionText,
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

fun ForecastDayWithDetails.toForecastDayDomain(appSettings: AppSettings): ForecastDay {
    return ForecastDay(
        dayName = getDayOfWeek(forecast.dateEpoch),
        maxTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.maxTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.maxTempF.roundToInt()
        },
        minTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.minTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.minTempF.roundToInt()
        },
        conditionCode = day.conditionCode,
        totalRainFull = when (appSettings.precipitationUnit) {
            PrecipitationUnit.Millimeters -> day.totalPrecipMm.roundToInt()
            PrecipitationUnit.Inches -> day.totalPrecipIn.roundToInt()
        },
    )
}