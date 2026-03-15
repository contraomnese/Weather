package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = MatchingLocationEntity.TABLE_NAME
)
data class MatchingLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = NETWORK_ID) val networkId: Int,
    @ColumnInfo(name = LATITUDE) val latitude: Double,
    @ColumnInfo(name = LONGITUDE) val longitude: Double,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = CITY) val city: String? = null,
    @ColumnInfo(name = STATE) val state: String? = null,
    @ColumnInfo(name = COUNTRY) val country: String? = null,
    @ColumnInfo(name = COUNTRY_CODE) val countryCode: String? = null,
) {
    companion object {
        const val TABLE_NAME = "matching_locations"
        const val NETWORK_ID = "network_id"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val NAME = "name"
        const val CITY = "city"
        const val STATE = "state"
        const val COUNTRY = "country"
        const val COUNTRY_CODE = "country_code"
    }

    fun toPoint(): String = "$latitude,$longitude"
}