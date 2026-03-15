package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.responses.OpenWeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherForecastApi {

    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Float = 52.52f,
        @Query("longitude") longitude: Float = 13.41f,
        @Query("current") current: String = "temperature_2m",
    ): Response<OpenWeatherForecastResponse>

}