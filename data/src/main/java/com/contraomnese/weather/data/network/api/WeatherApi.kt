package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.models.CurrentWeatherResponse
import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") query: String,
    ): Response<CurrentWeatherResponse>

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("q") query: String,
        @Query("days") days: Int = 3, // max on free pricing plan
        @Query("aqi") aqi: String = "yes",
        @Query("alerts") alerts: String = "yes",
        @Query("lang") lang: String,
    ): Response<ForecastWeatherResponse>
}