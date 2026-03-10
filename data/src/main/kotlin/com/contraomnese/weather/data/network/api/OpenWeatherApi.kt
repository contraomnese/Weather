package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.models.openw.ForecastOpenWeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("current") current: String = "temperature_2m",
    ): Response<ForecastOpenWeatherApiResponse>

}