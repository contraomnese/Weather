package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = ForecastLocationEntity.TABLE_NAME)
data class ForecastLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = LOCATION_ID) val locationId: Int = 0,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = REGION) val region: String,
    @ColumnInfo(name = COUNTRY) val country: String,
    @ColumnInfo(name = LATITUDE) val latitude: Double,
    @ColumnInfo(name = LONGITUDE) val longitude: Double,
    @ColumnInfo(name = LOCALTIME) val localtime: String,
    @ColumnInfo(name = LOCALTIME_EPOCH) val localtimeEpoch: Long,
    @ColumnInfo(name = TIME_ZONE_ID) val timeZoneId: String,
    @ColumnInfo(name = LAST_UPDATED) val lastUpdated: Long,
) {
    companion object {
        const val TABLE_NAME = "forecast_location"
        const val ID = "id"
        const val LOCATION_ID = "location_id"
        const val NAME = "name"
        const val REGION = "region"
        const val COUNTRY = "country"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val LOCALTIME = "localtime"
        const val LOCALTIME_EPOCH = "localtime_epoch"
        const val TIME_ZONE_ID = "time_zone_id"
        const val LAST_UPDATED = "last_updated"
    }
}
