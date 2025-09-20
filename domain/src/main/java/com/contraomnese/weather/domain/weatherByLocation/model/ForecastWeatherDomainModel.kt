package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
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
    val timeZone: TimeZone,
    val isSunUp: Boolean,
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

enum class CompactWeatherCondition {
    CLEAR, PARTLY_CLOUDY, CLOUDY, FOG, RAIN, SNOW, THUNDER, SLEET;

    companion object {
        fun fromConditionCode(code: Int): CompactWeatherCondition {
            return when (code) {
                1000 -> CLEAR
                1003 -> PARTLY_CLOUDY
                1006, 1009 -> CLOUDY
                1030, 1135, 1147 -> FOG
                1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195,
                1240, 1243, 1246, 1273, 1276,
                    -> RAIN

                1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225,
                1255, 1258, 1279, 1282,
                    -> SNOW

                1087 -> THUNDER

                1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207,
                1237, 1249, 1252, 1261, 1264,
                    -> SLEET

                else -> CLEAR

            }
        }
    }
}

data class CurrentInfo(
    val temperature: String,
    val feelsLikeTemperature: String,
    val isDay: Boolean,
    val condition: CompactWeatherCondition,
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
    val rainfallBeforeNow: List<Double>,
    val rainfallAfterNow: List<Double>,
    val rainfallNow: Double,
    val maxRainfall: Double,
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
    val condition: CompactWeatherCondition,
    val isDay: Boolean,
)

data class ForecastDay(
    val dayNumber: String,
    val dayName: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val condition: CompactWeatherCondition,
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
