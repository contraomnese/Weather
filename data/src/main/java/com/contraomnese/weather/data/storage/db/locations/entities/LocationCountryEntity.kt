package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocationCountryEntity.TABLE_NAME)
data class LocationCountryEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = NAME) val name: String,
) {
    companion object {
        const val TABLE_NAME = "countries"
        const val ID = "id"
        const val NAME = "name"
    }
}