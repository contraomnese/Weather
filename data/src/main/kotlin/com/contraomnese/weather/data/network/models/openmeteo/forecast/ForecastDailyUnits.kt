package com.contraomnese.weather.data.network.models.openmeteo.forecast

import com.google.gson.annotations.SerializedName

data class ForecastDailyUnits(
    val time: String,
    @SerializedName("temperature_2m_max")
    val temperature2mMax: String,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: String,
    @SerializedName("weather_code")
    val weatherCode: String,
    @SerializedName("apparent_temperature_max")
    val apparentTemperatureMax: String,
    @SerializedName("apparent_temperature_min")
    val apparentTemperatureMin: String,
    val sunrise: String,
    val sunset: String,
    @SerializedName("daylight_duration")
    val daylightDuration: String,
    @SerializedName("sunshine_duration")
    val sunshineDuration: String,
    @SerializedName("uv_index_max")
    val uvIndexMax: String,
    @SerializedName("uv_index_clear_sky_max")
    val uvIndexClearSkyMax: String,
    @SerializedName("rain_sum")
    val rainSum: String,
    @SerializedName("showers_sum")
    val showersSum: String,
    @SerializedName("snowfall_sum")
    val snowfallSum: String,
    @SerializedName("precipitation_sum")
    val precipitationSum: String,
    @SerializedName("precipitation_hours")
    val precipitationHours: String,
    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: String,
    @SerializedName("wind_speed_10m_max")
    val windSpeed10mMax: String,
    @SerializedName("wind_gusts_10m_max")
    val windGusts10mMax: String,
    @SerializedName("wind_direction_10m_dominant")
    val windDirection10mDominant: String,
)