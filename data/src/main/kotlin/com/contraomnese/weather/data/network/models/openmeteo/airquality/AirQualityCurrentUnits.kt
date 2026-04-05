package com.contraomnese.weather.data.network.models.openmeteo.airquality

import com.google.gson.annotations.SerializedName

data class AirQualityCurrentUnits(
    val time: String,
    val interval: String,
    @SerializedName("carbon_monoxide")
    val carbonMonoxide: String,
    @SerializedName("nitrogen_dioxide")
    val nitrogenDioxide: String,
    @SerializedName("pm2_5")
    val pm25: String,
    val pm10: String,
    @SerializedName("sulphur_dioxide")
    val sulphurDioxide: String,
    val ozone: String,
    @SerializedName("us_aqi")
    val usAqi: String,
    @SerializedName("european_aqi")
    val europeanAqi: String,
)