package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.datetime.TimeZone

data class Forecast(
    val location: ForecastLocation,
    val today: Weather,
    val forecast: ForecastWeather,
    val alerts: AlertsWeather,
)

data class ForecastLocation(
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val localTimeEpoch: Long,
    val localTime: LocationDateTime?,
    val timeZone: TimeZone,
    val isSunUp: Boolean,
)

data class ForecastWeather(
    val today: ForecastToday,
    val hours: List<ForecastHour>,
    val days: List<ForecastDay>,
)

data class AlertsWeather(
    val alerts: List<String>,
)
