package com.contraomnese.weather.data.network.models.openmeteo.airquality

import com.google.gson.annotations.SerializedName

data class AirQualityCurrent(
    val time: String,
    val interval: Int,
    @SerializedName("carbon_monoxide")
    val carbonMonoxide: Double,
    @SerializedName("nitrogen_dioxide")
    val nitrogenDioxide: Double,
    @SerializedName("pm2_5")
    val pm25: Double,
    val pm10: Double,
    @SerializedName("sulphur_dioxide")
    val sulphurDioxide: Double,
    val ozone: Double,
    @SerializedName("us_aqi")
    val usAqi: Int,
    @SerializedName("european_aqi")
    val europeanAqi: Int,
)