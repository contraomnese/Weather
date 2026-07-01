package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.datetime.TimeZone

data class FavoriteForecast(
    val locationId: Int,
    val locationName: String,
    val locationCountry: String,
    val timeZone: TimeZone,
    val temperature: String,
    val maxTemperature: String,
    val minTemperature: String,
    val condition: WeatherCondition,
    override val expiresAt: Long,
) : ExpirableData {
    override val updateTargetId: Int get() = locationId
}
