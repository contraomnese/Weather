package com.contraomnese.weather.data.network.models.openw

import com.google.gson.annotations.SerializedName

data class ForecastOpenWeatherApiResponse(
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