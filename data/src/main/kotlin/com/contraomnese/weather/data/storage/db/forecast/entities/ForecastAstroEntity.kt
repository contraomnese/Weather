package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = ForecastAstroEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastDailyEntity::class,
        parentColumns = [ForecastDailyEntity.ID],
        childColumns = [ForecastAstroEntity.FORECAST_DAILY_ID],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [ForecastAstroEntity.FORECAST_DAILY_ID], name = "forecast_astro_forecast_daily_id")]
)
data class ForecastAstroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = FORECAST_DAILY_ID) val forecastDailyId: Int,
    @ColumnInfo(name = SUNRISE) val sunrise: String,
    @ColumnInfo(name = SUNSET) val sunset: String,
    @ColumnInfo(name = IS_SUN_UP) val isSunUp: Int,
) {
    companion object {
        const val TABLE_NAME = "forecast_astro"
        const val FORECAST_DAILY_ID = "forecast_daily_id"
        const val SUNRISE = "sunrise"
        const val SUNSET = "sunset"
        const val IS_SUN_UP = "is_sun_up"
    }
}