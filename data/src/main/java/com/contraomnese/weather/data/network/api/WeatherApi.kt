package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.models.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") query: String,
    ): Response<CurrentWeatherResponse>
}