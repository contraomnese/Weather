package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.responses.OpenWeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherForecastApi {

    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String,
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timeformat") timeFormat: String = "unixtime",
        @Query("current") current: String = "temperature_2m,is_day,weather_code,wind_speed_10m,wind_direction_10m,wind_gusts_10m,surface_pressure,precipitation,relative_humidity_2m,cloud_cover,apparent_temperature,rain,showers,snowfall",
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,snow_depth,weather_code,surface_pressure,cloud_cover,visibility,wind_speed_10m,wind_direction_10m,wind_gusts_10m,is_day",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,uv_index_clear_sky_max,rain_sum,showers_sum,snowfall_sum,precipitation_sum,precipitation_hours,precipitation_probability_max,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant,visibility_mean,dew_point_2m_mean,relative_humidity_2m_mean,temperature_2m_mean",

    ): Response<OpenWeatherForecastResponse>

}