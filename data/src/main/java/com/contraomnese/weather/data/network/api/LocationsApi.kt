package com.contraomnese.weather.data.network.api

import com.contraomnese.weather.data.network.models.LocationNetwork
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationsApi {

    @GET("search")
    suspend fun getLocations(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("normalizeaddress") normalizeAddress: Int = 1,
    ): Response<List<LocationNetwork>>

}