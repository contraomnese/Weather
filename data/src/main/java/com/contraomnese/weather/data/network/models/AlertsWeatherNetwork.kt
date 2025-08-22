package com.contraomnese.weather.data.network.models

import com.google.gson.annotations.SerializedName


data class AlertsWeatherNetwork(
    val alert: List<AlertNetwork>,
)

data class AlertNetwork(
    @SerializedName("headline") val headline: String,
    @SerializedName("msgtype") val msgType: String,
    @SerializedName("severity") val severity: String,
    @SerializedName("urgency") val urgency: String,
    @SerializedName("areas") val areas: String,
    @SerializedName("category") val category: String,
    @SerializedName("certainty") val certainty: String,
    @SerializedName("event") val event: String,
    @SerializedName("note") val note: String,
    @SerializedName("effective") val effective: String,
    @SerializedName("expires") val expires: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("instruction") val instruction: String,
)