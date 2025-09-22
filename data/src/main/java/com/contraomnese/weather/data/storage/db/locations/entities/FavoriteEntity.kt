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
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = COUNTRY_NAME) val countryName: String,
    @ColumnInfo(name = LATITUDE) val latitude: Double,
    @ColumnInfo(name = LONGITUDE) val longitude: Double,
) {
    companion object {
        const val TABLE_NAME = "favorites"
        const val LOCATION_ID = "location_id"
        const val NAME = "name"
        const val COUNTRY_NAME = "country_name"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}