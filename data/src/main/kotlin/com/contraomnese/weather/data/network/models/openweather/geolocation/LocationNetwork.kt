package com.contraomnese.weather.data.network.models.openweather.geolocation

import com.google.gson.annotations.SerializedName

data class LocationNetwork(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    @SerializedName("feature_code")
    val featureCode: String, // Type of this location. Following the https://www.geonames.org/export/codes.html
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("admin1_id")
    val admin1Id: Int,
    @SerializedName("admin2_id")
    val admin2Id: Int,
    @SerializedName("admin3_id")
    val admin3Id: Int,
    @SerializedName("admin4_id")
    val admin4Id: Int,
    val timezone: String,
    val population: Int,
    val postcodes: List<String>,
    @SerializedName("country_id")
    val countryId: Int,
    val country: String,
    val admin1: String,
    val admin2: String,
    val admin3: String,
    val admin4: String,
)