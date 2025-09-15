package com.contraomnese.weather.data.network.models

import com.google.gson.annotations.SerializedName

data class ForecastCurrentNetwork(
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Long,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("temp_c")
    val tempC: Double,
    @SerializedName("temp_f")
    val tempF: Double,
    @SerializedName("is_day")
    val isDay: Int,
    val condition: ForecastConditionNetwork,
    @SerializedName("wind_mph")
    val windMph: Double,
    @SerializedName("wind_kph")
    val windKph: Double,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("pressure_mb")
    val pressureMb: Double,
    @SerializedName("pressure_in")
    val pressureIn: Double,
    @SerializedName("precip_mm")
    val precipMm: Double,
    @SerializedName("precip_in")
    val precipIn: Double,
    val humidity: Int,
    val cloud: Int,
    @SerializedName("feelslike_c")
    val feelsLikeC: Double,
    @SerializedName("feelslike_f")
    val feelsLikeF: Double,
    @SerializedName("windchill_c")
    val windChillC: Double,
    @SerializedName("windchill_f")
    val windChillF: Double,
    @SerializedName("heatindex_c")
    val heatIndexC: Double,
    @SerializedName("heatindex_f")
    val heatIndexF: Double,
    @SerializedName("dewpoint_c")
    val dewPointC: Float,
    @SerializedName("dewpoint_f")
    val dewPointF: Float,
    @SerializedName("vis_km")
    val visibilityKm: Double,
    @SerializedName("vis_miles")
    val visibilityMiles: Double,
    val uv: Double,
    @SerializedName("gust_mph")
    val gustMph: Double,
    @SerializedName("gust_kph")
    val gustKph: Double,
    @SerializedName("air_quality")
    val forecastAirQuality: ForecastAirQuality,
)

data class ForecastConditionNetwork(
    val text: String,
    val icon: String,
    val code: Int,
)

data class ForecastAirQuality(
    @SerializedName("co") val co: Float,
    @SerializedName("no2") val no2: Float,
    @SerializedName("o3") val o3: Float,
    @SerializedName("so2") val so2: Float,
    @SerializedName("pm2_5") val pm25: Float,
    @SerializedName("pm10") val pm10: Float,
    @SerializedName("us-epa-index") val usEpaIndex: Int,
    @SerializedName("gb-defra-index") val gbDefraIndex: Int,
)
