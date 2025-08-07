package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.ColumnInfo

data class WeatherLocationDto(
    @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = LATITUDE) val latitude: Double,
    @ColumnInfo(name = LONGITUDE) val longitude: Double,
) {
    companion object {
        const val ID = "alias_id"
        const val NAME = "alias_name"
        const val LATITUDE = "alias_latitude"
        const val LONGITUDE = "alias_longitude"
    }
}