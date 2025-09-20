package com.contraomnese.weather.data.mappers.forecast.internal

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastDayWithDetails
import com.contraomnese.weather.data.storage.db.forecast.entities.DayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.utils.getDayOfWeek
import com.contraomnese.weather.data.utils.getNumberOfMonth
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.internal.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.internal.ForecastToday
import kotlin.math.roundToInt

internal fun ForecastDayWithDetails.toForecastTodayDomain(appSettings: AppSettings): ForecastToday {

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

internal fun ForecastDayWithDetails.toForecastDayDomain(appSettings: AppSettings): ForecastDay {
    return ForecastDay(
        dayNumber = getNumberOfMonth(forecast.dateEpoch),
        dayName = getDayOfWeek(forecast.dateEpoch),
        maxTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.maxTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.maxTempF.roundToInt()
        },
        minTemperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> day.minTempC.roundToInt()
            TemperatureUnit.Fahrenheit -> day.minTempF.roundToInt()
        },
        condition = CompactWeatherCondition.fromConditionCode(day.conditionCode),
        totalRainFull = when (appSettings.precipitationUnit) {
            PrecipitationUnit.Millimeters -> day.totalPrecipMm.roundToInt()
            PrecipitationUnit.Inches -> day.totalPrecipIn.roundToInt()
        },
    )
}

internal fun ForecastDayNetwork.toForecastDayEntity(locationId: Int): ForecastDayEntity {

    return ForecastDayEntity(
        forecastLocationId = locationId,
        date = date,
        dateEpoch = dateEpoch,
    )
}

internal fun ForecastDayNetwork.toEntity(forecastDayId: Int) = DayEntity(
    forecastDayId = forecastDayId,
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
    conditionText = day.condition.text,
    conditionCode = day.condition.code,
    uv = day.uv,
    dayWillItRain = day.dailyWillItRain,
    dayChanceOfRain = day.dailyChanceOfRain,
    dayWillItSnow = day.dailyWillItSnow,
    dayChanceOfSnow = day.dailyChanceOfSnow,
)