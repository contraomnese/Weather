package com.contraomnese.weather.data.network.responses

import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastCurrentNetwork
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastCurrentUnits
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastDaily
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastDailyUnits
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastHourly
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastHourlyUnits
import com.google.gson.annotations.SerializedName

data class OpenMeteoForecastResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("generationtime_ms")
    val generationTimeMs: Double,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Double,
    @SerializedName("current_units")
    val currentUnits: ForecastCurrentUnits,
    val current: ForecastCurrentNetwork,
    @SerializedName("hourly_units")
    val hourlyUnits: ForecastHourlyUnits,
    val hourly: ForecastHourly,
    @SerializedName("daily_units")
    val dailyUnits: ForecastDailyUnits,
    val daily: ForecastDaily,
)