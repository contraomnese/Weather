package com.contraomnese.weather.data.network.models.openweather.forecast

import com.google.gson.annotations.SerializedName

data class ForecastCurrentUnits(
    val time: String,
    val interval: String,
    @SerializedName("temperature_2m")
    val temperature2m: String,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("weather_code")
    val weatherCode: String,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: String,
    @SerializedName("wind_direction_10m")
    val windDirection10m: String,
    @SerializedName("wind_gusts_10m")
    val windGusts10m: String,
    @SerializedName("surface_pressure")
    val surfacePressure: String,
    val precipitation: String,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: String,
    @SerializedName("cloud_cover")
    val cloudCover: String,
    @SerializedName("apparent_temperature")
    val apparentTemperature: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
)