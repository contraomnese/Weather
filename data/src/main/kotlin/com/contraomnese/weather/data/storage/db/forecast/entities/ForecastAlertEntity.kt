package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = ForecastAlertEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastLocationEntity::class,
        parentColumns = [ForecastLocationEntity.ID],
        childColumns = [ForecastAlertEntity.FORECAST_LOCATION_ID],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [ForecastAlertEntity.FORECAST_LOCATION_ID], name = "forecast_alert_forecast_location_id")]
)
data class ForecastAlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = FORECAST_LOCATION_ID) val forecastLocationId: Int,
    @ColumnInfo(name = HEADLINE) val headline: String,
    @ColumnInfo(name = MSG_TYPE) val msgType: String,
    @ColumnInfo(name = SEVERITY) val severity: String,
    @ColumnInfo(name = URGENCY) val urgency: String,
    @ColumnInfo(name = AREAS) val areas: String,
    @ColumnInfo(name = CATEGORY) val category: String,
    @ColumnInfo(name = CERTAINTY) val certainty: String,
    @ColumnInfo(name = EVENT) val event: String,
    @ColumnInfo(name = NOTE) val note: String,
    @ColumnInfo(name = EFFECTIVE) val effective: String,
    @ColumnInfo(name = EXPIRES) val expires: String,
    @ColumnInfo(name = DESC) val desc: String,
    @ColumnInfo(name = INSTRUCTION) val instruction: String,
) {
    companion object {
        const val TABLE_NAME = "forecast_alert"
        const val FORECAST_LOCATION_ID = "forecast_location_id"
        const val HEADLINE = "headline"
        const val MSG_TYPE = "msg_type"
        const val SEVERITY = "severity"
        const val URGENCY = "urgency"
        const val AREAS = "areas"
        const val CATEGORY = "category"
        const val CERTAINTY = "certainty"
        const val EVENT = "event"
        const val NOTE = "note"
        const val EFFECTIVE = "effective"
        const val EXPIRES = "expires"
        const val DESC = "desc"
        const val INSTRUCTION = "instruction"
    }
}