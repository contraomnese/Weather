package com.contraomnese.weather.data.network.responses

import com.contraomnese.weather.data.network.models.openmeteo.airquality.AirQualityCurrent
import com.contraomnese.weather.data.network.models.openmeteo.airquality.AirQualityCurrentUnits
import com.contraomnese.weather.data.network.models.openmeteo.airquality.AirQualityHour
import com.google.gson.annotations.SerializedName

data class OpenMeteoAirQualityResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Int,
    @SerializedName("current_units")
    val currentUnits: AirQualityCurrentUnits,
    val current: AirQualityCurrent,
    val hourly: AirQualityHour,
)