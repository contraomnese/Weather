package com.contraomnese.weather.data.network.responses

import com.contraomnese.weather.data.network.models.openweather.Current
import com.contraomnese.weather.data.network.models.openweather.CurrentUnits
import com.google.gson.annotations.SerializedName

data class OpenWeatherForecastResponse(
    val current: Current,
    @SerializedName("current_units")
    val currentUnits: CurrentUnits,
    val elevation: Double,
    @SerializedName("generationtime_ms")
    val generationTimeMs: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
)