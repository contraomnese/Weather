package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.collections.immutable.ImmutableList


data class Weather(
    val temperature: String,
    val feelsLikeTemperature: String,
    val isDay: Boolean,
    val condition: CompactWeatherCondition,
    val conditionText: String,
    val airQuality: AirQuality,
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
