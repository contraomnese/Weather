package com.contraomnese.weather.data.network.responses

import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastCurrentNetwork
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastCurrentUnits
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastDaily
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastDailyUnits
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastHourly
import com.contraomnese.weather.data.network.models.openweather.forecast.ForecastHourlyUnits
import com.google.gson.annotations.SerializedName

data class OpenWeatherForecastResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
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