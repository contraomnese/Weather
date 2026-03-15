package com.contraomnese.weather.data.network.models.openweather

import com.google.gson.annotations.SerializedName

data class CurrentUnits(
    val interval: String,
    @SerializedName("temperature_2m")
    val temperature: String,
    val time: String,
)