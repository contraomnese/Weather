package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = ForecastAstroEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastDayEntity::class,
        parentColumns = [ForecastDayEntity.ID],
        childColumns = [ForecastAstroEntity.FORECAST_DAY_ID],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [ForecastAstroEntity.FORECAST_DAY_ID], name = "forecast_astro_forecast_day_id")]
)
data class ForecastAstroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = FORECAST_DAY_ID) val forecastDayId: Int,
    @ColumnInfo(name = SUNRISE) val sunrise: String,
    @ColumnInfo(name = SUNSET) val sunset: String,
    @ColumnInfo(name = MOONRISE) val moonrise: String,
    @ColumnInfo(name = MOONSET) val moonset: String,
    @ColumnInfo(name = MOON_PHASE) val moonPhase: String,
    @ColumnInfo(name = MOON_ILLUMINATION) val moonIllumination: Int,
    @ColumnInfo(name = IS_MOON_UP) val isMoonUp: Int,
    @ColumnInfo(name = IS_SUN_UP) val isSunUp: Int,
) {
    companion object {
        const val TABLE_NAME = "forecast_astro"
        const val FORECAST_DAY_ID = "forecast_day_id"
        const val SUNRISE = "sunrise"
        const val SUNSET = "sunset"
        const val MOONRISE = "moonrise"
        const val MOONSET = "moonset"
        const val MOON_PHASE = "moon_phase"
        const val MOON_ILLUMINATION = "moon_illumination"
        const val IS_MOON_UP = "is_moon_up"
        const val IS_SUN_UP = "is_sun_up"
    }
}