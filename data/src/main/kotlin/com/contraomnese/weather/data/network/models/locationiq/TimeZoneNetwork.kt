package com.contraomnese.weather.data.network.models.locationiq

import com.google.gson.annotations.SerializedName


data class TimeZoneResponse(
    @SerializedName("timezone") val timezone: TimeZoneNetwork,
)

data class TimeZoneNetwork(
    @SerializedName("name") val name: String,
    @SerializedName("now_in_dst") val nowInDst: Int,
    @SerializedName("offset_sec") val offsetSec: Long,
    @SerializedName("short_name") val shortName: String,
    @SerializedName("full_name") val fullName: String,
)