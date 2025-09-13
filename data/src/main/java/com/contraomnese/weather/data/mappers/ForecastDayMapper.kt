package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import kotlin.math.roundToInt

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