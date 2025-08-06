package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = CityEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = CountryEntity::class,
        parentColumns = ["id"],
        childColumns = ["country_id"],
        onDelete = ForeignKey.NO_ACTION,
        onUpdate = ForeignKey.NO_ACTION
    )],
    indices = [
        Index(value = [CityEntity.COUNTRY_ID], name = "cities_test_ibfk_2")
    ]
)
data class CityEntity(
    @PrimaryKey val id: Int,

    @ColumnInfo(name = NAME) val name: String,

    @ColumnInfo(name = COUNTRY_ID) val countryId: Int,

    @ColumnInfo(name = LATITUDE) val latitude: Double,

    @ColumnInfo(name = LONGITUDE) val longitude: Double,

    ) {
    companion object {
        const val TABLE_NAME = "cities"
        const val ID = "id"
        const val NAME = "name"
        const val COUNTRY_ID = "country_id"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}
