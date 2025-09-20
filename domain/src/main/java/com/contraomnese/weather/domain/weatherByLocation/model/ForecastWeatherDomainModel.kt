package com.contraomnese.weather.domain.weatherByLocation.model

import com.contraomnese.weather.domain.weatherByLocation.model.internal.AirQualityInfo
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.internal.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.internal.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.internal.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.internal.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.internal.UvIndex
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.TimeZone


data class ForecastWeatherDomainModel(
    val locationInfo: LocationInfo,
    val currentInfo: CurrentInfo,
    val forecastInfo: ForecastInfo,
    val alertsInfo: AlertsInfo,
)

data class LocationInfo(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val localTimeEpoch: Long,
    val localTime: LocationDateTime?,
    val timeZone: TimeZone,
    val isSunUp: Boolean,
)

data class CurrentInfo(
    val temperature: String,
    val feelsLikeTemperature: String,
    val isDay: Boolean,
    val condition: CompactWeatherCondition,
    val conditionText: String,
    val airQuality: AirQualityInfo,
    val uvIndex: UvIndex,
    val windSpeed: String,
    val gustSpeed: String,
    val windDirection: String,
    val windDegree: Int,
    val humidity: Int,
    val dewPoint: Int,
    val pressure: Int,
    val isRainingExpected: Boolean,
    val rainfallBeforeNow: ImmutableList<Double>,
    val rainfallAfterNow: ImmutableList<Double>,
    val rainfallNow: Double,
    val maxRainfall: Double,
)

data class ForecastInfo(
    val today: ForecastToday,
    val forecastHours: ImmutableList<ForecastHour>,
    val forecastDays: ImmutableList<ForecastDay>,
)

data class AlertsInfo(
    val alerts: ImmutableList<String>,
)
