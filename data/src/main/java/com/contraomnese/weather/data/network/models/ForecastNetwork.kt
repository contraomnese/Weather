package com.contraomnese.weather.data.network.models

import com.google.gson.annotations.SerializedName


data class ForecastNetwork(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDayNetwork>,
)

data class ForecastDayNetwork(
    val date: String,
    @SerializedName("date_epoch")
    val dateEpoch: Long,
    val day: DayNetwork,
    val astro: AstroNetwork,
    val hour: List<HourNetwork>,
)

data class DayNetwork(
    @SerializedName("maxtemp_c")
    val maxTempC: Double,
    @SerializedName("maxtemp_f")
    val maxTempF: Double,
    @SerializedName("mintemp_c")
    val minTempC: Double,
    @SerializedName("mintemp_f")
    val minTempF: Double,
    @SerializedName("avgtemp_c")
    val avgTempC: Double,
    @SerializedName("avgtemp_f")
    val avgTempF: Double,
    @SerializedName("maxwind_mph")
    val maxWindMph: Double,
    @SerializedName("maxwind_kph")
    val maxWindKph: Double,
    @SerializedName("totalprecip_mm")
    val totalPrecipMm: Double,
    @SerializedName("totalprecip_in")
    val totalPrecipIn: Double,
    @SerializedName("totalsnow_cm")
    val totalSnowCm: Double,
    @SerializedName("avgvis_km")
    val avgVisKm: Double,
    @SerializedName("avgvis_miles")
    val avgVisMiles: Double,
    @SerializedName("avghumidity")
    val avgHumidity: Int,
    @SerializedName("daily_will_it_rain")
    val dailyWillItRain: Int,
    @SerializedName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @SerializedName("daily_will_it_snow")
    val dailyWillItSnow: Int,
    @SerializedName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    val condition: ForecastConditionNetwork,
    val uv: Double,
)

data class AstroNetwork(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerializedName("moon_phase")
    val moonPhase: String,
    @SerializedName("moon_illumination")
    val moonIllumination: Int,
    @SerializedName("is_moon_up")
    val isMoonUp: Int,
    @SerializedName("is_sun_up")
    val isSunUp: Int,
)

data class HourNetwork(
    @SerializedName("time_epoch")
    val timeEpoch: Long,
    val time: String,
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
    @SerializedName("snow_cm")
    val snowCm: Double,
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
    val dewPointC: Double,
    @SerializedName("dewpoint_f")
    val dewPointF: Double,
    @SerializedName("will_it_rain")
    val willItRain: Int,
    @SerializedName("chance_of_rain")
    val chanceOfRain: Int,
    @SerializedName("will_it_snow")
    val willItSnow: Int,
    @SerializedName("chance_of_snow")
    val chanceOfSnow: Int,
    @SerializedName("vis_km")
    val visibilityKm: Double,
    @SerializedName("vis_miles")
    val visibilityMiles: Double,
    @SerializedName("gust_mph")
    val gustMph: Double,
    @SerializedName("gust_kph")
    val gustKph: Double,
    val uv: Double,
)