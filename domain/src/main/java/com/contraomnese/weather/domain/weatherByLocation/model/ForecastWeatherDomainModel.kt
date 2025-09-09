package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

data class ForecastWeatherDomainModel(
    val locationInfo: LocationInfo,
    val currentInfo: CurrentInfo,
    val forecastInfo: ForecastInfo,
    val alertsInfo: AlertsInfo,
)

data class LocationInfo(
    val locationTimeEpoch: Long,
    val locationTime: LocationDateTime?,
    val isAfterMidDay: Boolean?,
)

data class LocationDateTime(
    val dateTime: LocalDateTime,
) {
    private val localTimeFormatter = LocalDateTime.Format {
        hour()
        char(':')
        minute()
    }

    fun toLocalTime(): String {
        return dateTime.format(localTimeFormatter)
    }

    fun toMinutes(): Int {
        return dateTime.hour * 60 + dateTime.minute
    }
}

data class LocationTime(
    val time: LocalTime,
) {
    private val localTimeFormatter = LocalTime.Format {
        hour()
        char(':')
        minute()
    }

    fun toLocalTime(): String {
        return time.format(localTimeFormatter)
    }

    fun toMinutes(): Int {
        return time.hour * 60 + time.minute
    }
}

data class CurrentInfo(
    val temperature: String,
    val feelsLike: String,
    val isDay: Boolean,
    val conditionCode: Int,
    val conditionText: String,
    val airQualityIndex: AirQualityInfo,
    val uvIndex: UvIndex,
    val windSpeed: String,
    val gustSpeed: String,
    val windDirection: String,
    val windDegree: Int,
    val humidity: Int,
    val dewPoint: Int,
    val pressure: Int,
    val isRainingExpected: Boolean,
    val rainfallLast24Hours: Double,
    val rainfallNext24Hours: Double,
    val rainfallNextHour: Double,
)

@JvmInline
value class UvIndex(val value: Int)

data class ForecastInfo(
    val today: ForecastToday,
    val forecastHours: ImmutableList<ForecastHour>,
    val forecastDays: ImmutableList<ForecastDay>,
)

data class ForecastToday(
    val maxTemperature: String,
    val minTemperature: String,
    val conditionCode: Int,
    val conditionText: String,
    val totalUvIndex: String,
    val rainChance: String,
    val totalRainFull: String,
    val sunrise: LocationTime?,
    val sunset: LocationTime?,
)

data class ForecastHour(
    val time: String,
    val temperature: String,
    val conditionCode: Int,
    val isDay: Boolean,
)

data class ForecastDay(
    val dayName: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val conditionCode: Int,
    val totalRainFull: Int,
)

data class AlertsInfo(
    val alerts: ImmutableList<String>,
)

data class AirQualityInfo(
    val aqiIndex: Int,
    val aqiText: String,
    val coLevel: PollutantLevel,
    val no2Level: PollutantLevel,
    val o3Level: PollutantLevel,
    val so2Level: PollutantLevel,
    val pm25Level: PollutantLevel,
    val pm10Level: PollutantLevel,
)

sealed interface PollutantLevel {
    data object Good : PollutantLevel
    data object Moderate : PollutantLevel
    data object Bad : PollutantLevel
}
