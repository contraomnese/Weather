package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = CityEntity.TABLE_NAME,
    indices = [
        Index(value = [CityEntity.COUNTRY_ID], name = "cities_test_ibfk_2"),
        Index(value = [CityEntity.STATE_ID], name = "cities_test_ibfk_1")
    ]
)
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = ID) val id: Int,

    @ColumnInfo(name = NAME) val name: String,

    @ColumnInfo(name = STATE_ID) val stateId: Int,

    @ColumnInfo(name = STATE_CODE) val stateCode: String,

    @ColumnInfo(name = COUNTRY_ID) val countryId: Int,

    @ColumnInfo(name = COUNTRY_CODE) val countryCode: String,

    @ColumnInfo(name = LATITUDE) val latitude: Double,

    @ColumnInfo(name = LONGITUDE) val longitude: Double,

    @ColumnInfo(name = CREATED_AT) val createdAt: String,

    @ColumnInfo(name = UPDATED_AT) val updatedAt: String,

    @ColumnInfo(name = FLAG) val flag: Int,

    @ColumnInfo(name = WIKI_DATA_ID) val wikiDataId: String?,

    ) {
    companion object {
        const val TABLE_NAME = "cities"
        const val ID = "id"
        const val NAME = "name"
        const val STATE_ID = "state_id"
        const val STATE_CODE = "state_code"
        const val COUNTRY_ID = "country_id"
        const val COUNTRY_CODE = "country_code"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
        const val FLAG = "flag"
        const val WIKI_DATA_ID = "wikiDataId"
    }
}
