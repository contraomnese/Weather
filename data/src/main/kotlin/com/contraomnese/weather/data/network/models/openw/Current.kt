package com.contraomnese.weather.data.network.models.openw

import com.google.gson.annotations.SerializedName

data class Current(
    val interval: Int,
    @SerializedName("temperature_2m")
    val temperature: Double,
    val time: String,
)