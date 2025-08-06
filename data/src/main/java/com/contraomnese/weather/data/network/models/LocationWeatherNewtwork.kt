package com.contraomnese.weather.data.network.models

import com.google.gson.annotations.SerializedName

data class LocationWeatherNetwork(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id")
    val timeZoneId: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,
    val localtime: String,
)