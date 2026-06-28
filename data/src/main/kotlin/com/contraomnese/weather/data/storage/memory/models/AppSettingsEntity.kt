package com.contraomnese.weather.data.storage.memory.models

data class AppSettingsEntity(
    val language: String,
    val timezone: String,
    val speedUnit: String,
    val precipitationUnit: String,
    val temperatureUnit: String,
    val pressureUnit: String,
    val favoritesForecastUpdateEnabled: Boolean,
    val favoritesForecastUpdateInterval: Long,
    val favoritesForecastNotificationEnabled: Boolean,
)