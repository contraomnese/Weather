package com.contraomnese.weather.data.network.models.openmeteo.forecast


import com.google.gson.annotations.SerializedName

data class ForecastHourly(
    val time: List<Long>,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double>,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: List<Int>,
    @SerializedName("dew_point_2m")
    val dewPoint2m: List<Double>,
    @SerializedName("apparent_temperature")
    val apparentTemperature: List<Double>,
    @SerializedName("precipitation_probability")
    val precipitationProbability: List<Int>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    @SerializedName("snow_depth")
    val snowDepth: List<Double>,
    @SerializedName("weather_code")
    val weatherCode: List<Int>,
    @SerializedName("surface_pressure")
    val surfacePressure: List<Double>,
    @SerializedName("cloud_cover")
    val cloudCover: List<Int>,
    val visibility: List<Double>,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: List<Double>,
    @SerializedName("wind_direction_10m")
    val windDirection10m: List<Int>,
    @SerializedName("wind_gusts_10m")
    val windGusts10m: List<Double>,
    @SerializedName("is_day")
    val isDay: List<Int>,
)