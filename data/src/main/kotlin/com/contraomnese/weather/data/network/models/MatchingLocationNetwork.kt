package com.contraomnese.weather.data.network.models

import com.google.gson.annotations.SerializedName

data class MatchingLocationNetwork(

    @SerializedName("place_id") val placeId: Long,
    val licence: String,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("osm_id") val osmId: Long,
    @SerializedName("boundingbox") val boundingBox: List<String>,
    val lat: String,
    val lon: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("class") val clazz: String,
    val type: String,
    val importance: Double,
    val icon: String,
    val address: Address,
)

data class Address(
    val name: String?,
    @SerializedName("house_number") val houseNumber: String?,
    val road: String?,
    val neighbourhood: String?,
    val suburb: String?,
    val island: String?,
    val city: String?,
    val county: String?,
    val state: String?,
    @SerializedName("state_code") val stateCode: String?,
    val postcode: String?,
    val country: String?,
    @SerializedName("country_code") val countryCode: String?,
)
