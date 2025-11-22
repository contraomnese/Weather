package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = FavoriteEntity.TABLE_NAME
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = LOCATION_ID) val locationId: Int,
    @ColumnInfo(name = LATITUDE) val latitude: Double,
    @ColumnInfo(name = LONGITUDE) val longitude: Double,
    @ColumnInfo(name = CITY) val city: String?,
    @ColumnInfo(name = STATE) val state: String?,
    @ColumnInfo(name = COUNTRY) val country: String?,
) {
    companion object {
        const val TABLE_NAME = "favorites"
        const val LOCATION_ID = "location_id"
        const val CITY = "city"
        const val STATE = "state"
        const val COUNTRY = "country"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}