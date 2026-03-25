package com.contraomnese.weather.data.network.models.openweather.forecast

import com.google.gson.annotations.SerializedName

data class ForecastDaily(
    val time: List<Long>,
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double>,
    @SerializedName("weather_code")
    val weatherCode: List<Int>,
    @SerializedName("apparent_temperature_max")
    val apparentTemperatureMax: List<Double>,
    @SerializedName("apparent_temperature_min")
    val apparentTemperatureMin: List<Double>,
    val sunrise: List<Long>,
    val sunset: List<Long>,
    @SerializedName("daylight_duration")
    val daylightDuration: List<Double>,
    @SerializedName("sunshine_duration")
    val sunshineDuration: List<Double>,
    @SerializedName("uv_index_max")
    val uvIndexMax: List<Double>,
    @SerializedName("uv_index_clear_sky_max")
    val uvIndexClearSkyMax: List<Double>,
    @SerializedName("rain_sum")
    val rainSum: List<Double>,
    @SerializedName("showers_sum")
    val showersSum: List<Double>,
    @SerializedName("snowfall_sum")
    val snowfallSum: List<Double>,
    @SerializedName("precipitation_sum")
    val precipitationSum: List<Double>,
    @SerializedName("precipitation_hours")
    val precipitationHours: List<Double>,
    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: List<Int>,
    @SerializedName("wind_speed_10m_max")
    val windSpeed10mMax: List<Double>,
    @SerializedName("wind_gusts_10m_max")
    val windGusts10mMax: List<Double>,
    @SerializedName("wind_direction_10m_dominant")
    val windDirection10mDominant: List<Int>,
    @SerializedName("visibility_mean")
    val visibilityMean: List<Double>,
    @SerializedName("dew_point_2m_mean")
    val dewPoint2mMean: List<Double>,
    @SerializedName("relative_humidity_2m_mean")
    val relativeHumidity2mMean: List<Double>,
    @SerializedName("temperature_2m_mean")
    val temperature2mMean: List<Double>,
)