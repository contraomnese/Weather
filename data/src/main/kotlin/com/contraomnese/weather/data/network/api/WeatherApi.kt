package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.models.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("q") query: String,
        @Query("days") days: Int = 3, // max on free pricing plan
        @Query("aqi") aqi: String = "yes",
        @Query("alerts") alerts: String = "yes",
    ): Response<ForecastResponse>
}