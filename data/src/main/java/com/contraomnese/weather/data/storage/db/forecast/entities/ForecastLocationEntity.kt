package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.contraomnese.weather.data.storage.db.locations.entities.LocationEntity


@Entity(
    tableName = ForecastLocationEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = LocationEntity::class,
        parentColumns = [LocationEntity.ID],
        childColumns = [ForecastLocationEntity.LOCATION_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ForecastLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = LOCATION_ID) val locationId: Int = 0,
    @ColumnInfo(name = LOCALTIME) val localtime: String,
    @ColumnInfo(name = LOCALTIME_EPOCH) val localtimeEpoch: Long,
    @ColumnInfo(name = LAST_UPDATED) val lastUpdated: Long,
) {
    companion object {
        const val TABLE_NAME = "forecast_location"
        const val ID = "id"
        const val LOCATION_ID = "location_id"
        const val LOCALTIME = "localtime"
        const val LOCALTIME_EPOCH = "localtime_epoch"
        const val LAST_UPDATED = "last_updated"
    }
}
