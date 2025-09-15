package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = LocationEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = LocationCountryEntity::class,
        parentColumns = [LocationCountryEntity.ID],
        childColumns = [LocationEntity.COUNTRY_ID],
        onDelete = ForeignKey.NO_ACTION,
        onUpdate = ForeignKey.NO_ACTION
    )],
    indices = [
        Index(value = [LocationEntity.COUNTRY_ID], name = "cities_test_ibfk_2")
    ]
)
data class LocationEntity(
    @PrimaryKey val id: Int,

    @ColumnInfo(name = NAME) val name: String,

    @ColumnInfo(name = COUNTRY_ID) val countryId: Int,

    @ColumnInfo(name = LATITUDE) val latitude: Double,

    @ColumnInfo(name = LONGITUDE) val longitude: Double,

    ) {
    companion object {
        const val TABLE_NAME = "locations"
        const val ID = "id"
        const val NAME = "name"
        const val COUNTRY_ID = "country_id"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}
