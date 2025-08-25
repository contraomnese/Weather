package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.collections.immutable.ImmutableList

data class ForecastWeatherDomainModel(
    val locationInfo: LocationInfo,
    val currentInfo: CurrentInfo,
    val forecastInfo: ForecastInfo,
    val alertsInfo: AlertsInfo,
)

data class LocationInfo(
    val locationTimeEpoch: Long,
    val locationTime: String,
)

data class CurrentInfo(
    val temperature: String,
    val feelsLike: String,
    val isDay: Boolean,
    val conditionCode: Int,
    val conditionText: String,
    val windSpeed: String,
    val windDirection: String,
    val windDegree: Int,
    val pressure: String,
    val humidity: String,
    val uvIndex: String,
    val airQualityIndex: Int,
)

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
    val sunrise: String,
    val sunset: String,
)

data class ForecastHour(
    val time: String,
    val temperature: String,
    val conditionCode: Int,
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
