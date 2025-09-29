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
    @ColumnInfo(name = TYPE) val type: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = HOUSE_NUMBER) val houseNumber: String,
    @ColumnInfo(name = ROAD) val road: String,
    @ColumnInfo(name = NEIGHBOURHOOD) val neighbourhood: String,
    @ColumnInfo(name = SUBURB) val suburb: String,
    @ColumnInfo(name = ISLAND) val island: String,
    @ColumnInfo(name = CITY) val city: String,
    @ColumnInfo(name = COUNTY) val county: String,
    @ColumnInfo(name = STATE) val state: String,
    @ColumnInfo(name = STATE_CODE) val stateCode: String,
    @ColumnInfo(name = POSTCODE) val postcode: String,
    @ColumnInfo(name = COUNTRY) val country: String,
    @ColumnInfo(name = COUNTRY_CODE) val countryCode: String,
) {
    companion object {
        const val TABLE_NAME = "matching_locations"
        const val NETWORK_ID = "network_id"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val TYPE = "type"
        const val NAME = "name"
        const val HOUSE_NUMBER = "house_number"
        const val ROAD = "road"
        const val NEIGHBOURHOOD = "neighbourhood"
        const val SUBURB = "suburb"
        const val ISLAND = "island"
        const val CITY = "city"
        const val COUNTY = "county"
        const val STATE = "state"
        const val STATE_CODE = "state_code"
        const val POSTCODE = "postcode"
        const val COUNTRY = "country"
        const val COUNTRY_CODE = "country_code"
    }

    fun toPoint(): String = "$latitude,$longitude"
}