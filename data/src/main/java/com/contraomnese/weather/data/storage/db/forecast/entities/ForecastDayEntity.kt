package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = ForecastDayEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastLocationEntity::class,
        parentColumns = [ForecastLocationEntity.ID],
        childColumns = [ForecastDayEntity.FORECAST_LOCATION_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ForecastDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = FORECAST_LOCATION_ID) val forecastLocationId: Int,
    @ColumnInfo(name = DATE) val date: String,
    @ColumnInfo(name = DATE_EPOCH) val dateEpoch: Long,
) {
    companion object {
        const val TABLE_NAME = "forecast_day"
        const val ID = "id"
        const val FORECAST_LOCATION_ID = "forecast_location_id"
        const val DATE = "date"
        const val DATE_EPOCH = "date_epoch"
    }
}