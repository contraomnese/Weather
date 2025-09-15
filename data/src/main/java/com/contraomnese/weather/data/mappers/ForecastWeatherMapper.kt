package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.data.storage.db.forecast.dao.LocationWithForecasts
import com.contraomnese.weather.data.storage.db.forecast.entities.DayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastCurrentEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.HourlyForecastEntity
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

fun ForecastDayNetwork.toForecastDayEntity(locationId: Int): ForecastDayEntity {

    return ForecastDayEntity(
        forecastLocationId = locationId,
        date = date,
        dateEpoch = dateEpoch,
    )
}

fun ForecastDayNetwork.toEntity(forecastDayId: Int) = DayEntity(
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

fun LocationWithForecasts.toDomain(appSettings: AppSettings): ForecastWeatherDomainModel {

    val nextDayHourLimit = location.localtimeEpoch + HOURS * MINUTES * SECONDS
    val forecastHours =
        mutableListOf(forecastDays.first().hour.last { it.timeEpoch <= location.localtimeEpoch })
    forecastHours.addAll(forecastDays.first().hour.filter { it.timeEpoch > location.localtimeEpoch })
    forecastHours.addAll(forecastDays.drop(1).first().hour.filter { it.timeEpoch <= nextDayHourLimit })

    val locationTime = DateTimeParser.parseIso(location.localtime)
    val isAfterMidDay = locationTime?.let { locTime ->
        val sunrise = DateTimeParser.parseAmPmTime(forecastDays.first().astro.sunrise)
        val sunset = DateTimeParser.parseAmPmTime(forecastDays.first().astro.sunset)
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
            temperature = forecastCurrent.toTemperatureDomain(appSettings.temperatureUnit),
            feelsLike = when (appSettings.temperatureUnit) {
                TemperatureUnit.Celsius -> forecastCurrent.feelsLikeC.roundToInt().toString()
                TemperatureUnit.Fahrenheit -> forecastCurrent.feelsLikeF.roundToInt().toString()
            },
            isDay = forecastCurrent.isDay == IS_DAY,
            conditionCode = forecastCurrent.conditionCode,
            conditionText = forecastCurrent.conditionText,
            windSpeed = forecastCurrent.toWindDomain(appSettings.windSpeedUnit),
            gustSpeed = forecastCurrent.toGustDomain(appSettings.windSpeedUnit),
            windDirection = forecastCurrent.windDir.translateDirection(),
            windDegree = forecastCurrent.windDegree,
            pressure = forecastCurrent.toPressureDomain(appSettings.pressureUnit),
            isRainingExpected = forecastDays.first().day.dayWillItRain == DAILY_WILL_RAIN,
            rainfallLast24Hours = forecastDays[0].hour.sumOf { hour ->
                if (hour.timeEpoch < location.localtimeEpoch) hour.toPrecipitationDomain(
                    appSettings.precipitationUnit
                ) else DEFAULT_RAINFALL
            },
            rainfallNext24Hours = forecastDays[0].hour.sumOf { hour ->
                if (hour.timeEpoch >= location.localtimeEpoch) hour.toPrecipitationDomain(
                    appSettings.precipitationUnit
                ) else DEFAULT_RAINFALL
            } +
                    forecastDays[1].hour.sumOf { hour ->
                        if (hour.timeEpoch < nextDayHourLimit) hour.toPrecipitationDomain(
                            appSettings.precipitationUnit
                        ) else DEFAULT_RAINFALL
                    },
            rainfallNextHour = forecastCurrent.toPrecipitationDomain(appSettings.precipitationUnit),
            humidity = forecastCurrent.humidity,
            dewPoint = forecastCurrent.toDewPoint(appSettings.temperatureUnit),
            uvIndex = UvIndex(forecastCurrent.uv.roundToInt()),
            airQualityIndex = forecastCurrent.toAirQualityInfo()
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

private fun ForecastCurrentEntity.toTemperatureDomain(temperatureUnit: TemperatureUnit): String {
    return when (temperatureUnit) {
        TemperatureUnit.Celsius -> this.tempC.roundToInt().toString()
        TemperatureUnit.Fahrenheit -> this.tempF.roundToInt().toString()
    }
}

private fun ForecastCurrentEntity.toWindDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.windKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.windMph.roundToInt().toString()
        WindSpeedUnit.Ms -> this.windKph.toMs().roundToInt().toString()
    }
}

private fun ForecastCurrentEntity.toGustDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.gustKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.gustMph.roundToInt().toString()
        WindSpeedUnit.Ms -> this.gustKph.toMs().roundToInt().toString()
    }
}

private fun ForecastCurrentEntity.toPressureDomain(pressureUnit: PressureUnit): Int {
    return when (pressureUnit) {
        PressureUnit.MmHg -> (this.pressureIn * MM_IN_INCH).roundToInt()
        PressureUnit.GPa -> this.pressureMb.roundToInt()
    }
}

private fun ForecastCurrentEntity.toDewPoint(temperatureUnit: TemperatureUnit): Int {
    return when (temperatureUnit) {
        TemperatureUnit.Celsius -> this.dewPointC.roundToInt()
        TemperatureUnit.Fahrenheit -> this.dewPointF.roundToInt()
    }
}

private fun ForecastCurrentEntity.toPrecipitationDomain(precipitationUnit: PrecipitationUnit): Double {
    return when (precipitationUnit) {
        PrecipitationUnit.Millimeters -> this.precipMm
        PrecipitationUnit.Inches -> this.precipIn
    }
}

private fun HourlyForecastEntity.toPrecipitationDomain(precipitationUnit: PrecipitationUnit): Double {
    return when (precipitationUnit) {
        PrecipitationUnit.Millimeters -> this.precipMm
        PrecipitationUnit.Inches -> this.precipIn
    }
}



