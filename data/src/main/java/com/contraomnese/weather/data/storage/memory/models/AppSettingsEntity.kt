package com.contraomnese.weather.data.storage.memory.models

data class AppSettingsEntity(
    val language: String,
    val speedUnit: String,
    val precipitationUnit: String,
    val temperatureUnit: String,
    val visibilityUnit: String,
    val pressureUnit: String,
)