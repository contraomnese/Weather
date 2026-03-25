package com.contraomnese.weather.data.network.models.openweather.forecast

import com.google.gson.annotations.SerializedName

data class ForecastCurrentNetwork(
    val time: Long,
    val interval: Int,
    @SerializedName("temperature_2m")
    val temperature2m: Double,
    @SerializedName("is_day")
    val isDay: Int,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double,
    @SerializedName("wind_direction_10m")
    val windDirection10m: Int,
    @SerializedName("wind_gusts_10m")
    val windGusts10m: Double,
    @SerializedName("surface_pressure")
    val surfacePressure: Double,
    val precipitation: Double,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: Int,
    @SerializedName("cloud_cover")
    val cloudCover: Int,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
)