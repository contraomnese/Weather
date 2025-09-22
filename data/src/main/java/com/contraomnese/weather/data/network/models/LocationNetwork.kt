package com.contraomnese.weather.data.network.models

import com.google.gson.annotations.SerializedName

data class LocationNetwork(

    @SerializedName("place_id") val placeId: Long,
    val licence: String,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("osm_id") val osmId: Long,
    val lat: String,
    val lon: String,
    @SerializedName("class") val clazz: String,
    val type: String,
    @SerializedName("place_rank") val placeRank: Int,
    val importance: Double,
    @SerializedName("addresstype") val addressType: String,
    val name: String,
    @SerializedName("display_name") val displayName: String,
    val address: Address?,
    @SerializedName("boundingbox") val boundingBox: List<String>,
)

data class Address(
    val state: String,
    @SerializedName("ISO3166-2-lvl4") val iso: String,
    val region: String,
    val country: String,
    val countryCode: String,
)
