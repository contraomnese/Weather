package com.contraomnese.weather.data.storage.db.locations.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = FavoriteEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = LocationEntity::class,
        parentColumns = [LocationEntity.ID],
        childColumns = [FavoriteEntity.LOCATION_ID],
        onDelete = ForeignKey.NO_ACTION,
        onUpdate = ForeignKey.NO_ACTION
    )],
    indices = [
        Index(value = [FavoriteEntity.LOCATION_ID], name = "favorites_test_ibfk_2")
    ]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = LOCATION_ID) val cityId: Int,
) {
    companion object {
        const val TABLE_NAME = "favorites"
        const val LOCATION_ID = "location_id"
    }
}