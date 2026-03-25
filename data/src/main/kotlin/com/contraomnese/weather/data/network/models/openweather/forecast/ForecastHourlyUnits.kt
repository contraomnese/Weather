package com.contraomnese.weather.data.network.models.openweather.forecast


import com.google.gson.annotations.SerializedName

data class ForecastHourlyUnits(
    val time: String,
    @SerializedName("temperature_2m")
    val temperature2m: String,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: String,
    @SerializedName("dew_point_2m")
    val dewPoint2m: String,
    @SerializedName("apparent_temperature")
    val apparentTemperature: String,
    @SerializedName("precipitation_probability")
    val precipitationProbability: String,
    val precipitation: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    @SerializedName("snow_depth")
    val snowDepth: String,
    @SerializedName("weather_code")
    val weatherCode: String,
    @SerializedName("surface_pressure")
    val surfacePressure: String,
    @SerializedName("cloud_cover")
    val cloudCover: String,
    val visibility: String,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: String,
    @SerializedName("wind_direction_10m")
    val windDirection10m: String,
    @SerializedName("wind_gusts_10m")
    val windGusts10m: String,
)