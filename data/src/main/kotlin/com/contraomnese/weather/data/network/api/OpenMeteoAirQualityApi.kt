package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.responses.OpenMeteoAirQualityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoAirQualityApi {

    @GET("air-quality")
    suspend fun getAirQuality(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "carbon_monoxide,nitrogen_dioxide,pm2_5,pm10,sulphur_dioxide,ozone,us_aqi,uv_index",
        @Query("hourly") hourly: String = "uv_index",
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timeformat") timeFormat: String = "unixtime",
        @Query("timezone") timezone: String,
    ): Response<OpenMeteoAirQualityResponse>

}