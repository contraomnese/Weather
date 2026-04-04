package com.contraomnese.weather.data.network.models.openweather.airquality

import com.google.gson.annotations.SerializedName

data class AirQualityHour(
    val time: List<Double>,
    @SerializedName("uv_index")
    val uvIndex: List<Double?>,
)