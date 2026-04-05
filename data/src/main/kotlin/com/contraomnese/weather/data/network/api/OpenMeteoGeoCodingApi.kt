package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.responses.OpenMeteoLocationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoGeoCodingApi {

    @GET("search")
    suspend fun getLocations(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "ru",
        @Query("format") format: String = "json",
    ): Response<OpenMeteoLocationsResponse>

}