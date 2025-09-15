package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = DayEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastDayEntity::class,
        parentColumns = [ForecastDayEntity.ID],
        childColumns = [DayEntity.FORECAST_DAY_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DayEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = FORECAST_DAY_ID) val forecastDayId: Int,
    @ColumnInfo(name = MAX_TEMP_C) val maxTempC: Double,
    @ColumnInfo(name = MIN_TEMP_C) val minTempC: Double,
    @ColumnInfo(name = MAX_TEMP_F) val maxTempF: Double,
    @ColumnInfo(name = MIN_TEMP_F) val minTempF: Double,
    @ColumnInfo(name = AVG_TEMP_C) val avgTempC: Double,
    @ColumnInfo(name = AVG_TEMP_F) val avgTempF: Double,
    @ColumnInfo(name = MAX_WIND_MPH) val maxWindMph: Double,
    @ColumnInfo(name = MAX_WIND_KPH) val maxWindKph: Double,
    @ColumnInfo(name = TOTAL_PRECIP_MM) val totalPrecipMm: Double,
    @ColumnInfo(name = TOTAL_PRECIP_IN) val totalPrecipIn: Double,
    @ColumnInfo(name = TOTAL_SNOW_CM) val totalSnowCm: Double,
    @ColumnInfo(name = AVG_VIS_KM) val avgVisKm: Double,
    @ColumnInfo(name = AVG_VIS_MILES) val avgVisMiles: Double,
    @ColumnInfo(name = AVG_HUMIDITY) val avgHumidity: Int,
    @ColumnInfo(name = CONDITION_TEXT) val conditionText: String,
    @ColumnInfo(name = CONDITION_CODE) val conditionCode: Int,
    @ColumnInfo(name = UV) val uv: Double,
    @ColumnInfo(name = DAY_WILL_IT_RAIN) val dayWillItRain: Int,
    @ColumnInfo(name = DAY_CHANCE_OF_RAIN) val dayChanceOfRain: Int,
    @ColumnInfo(name = DAY_WILL_IT_SNOW) val dayWillItSnow: Int,
    @ColumnInfo(name = DAY_CHANCE_OF_SNOW) val dayChanceOfSnow: Int,
) {
    companion object {
        const val TABLE_NAME = "day"
        const val FORECAST_DAY_ID = "forecast_day_id"
        const val MAX_TEMP_C = "max_temp_c"
        const val MIN_TEMP_C = "min_temp_c"
        const val MAX_TEMP_F = "max_temp_f"
        const val MIN_TEMP_F = "min_temp_f"
        const val AVG_TEMP_C = "avg_temp_c"
        const val AVG_TEMP_F = "avg_temp_f"
        const val MAX_WIND_MPH = "max_wind_mph"
        const val MAX_WIND_KPH = "max_wind_kph"
        const val TOTAL_PRECIP_MM = "total_precip_mm"
        const val TOTAL_PRECIP_IN = "total_precip_in"
        const val TOTAL_SNOW_CM = "total_snow_cm"
        const val AVG_VIS_KM = "avg_vis_km"
        const val AVG_VIS_MILES = "avg_vis_miles"
        const val AVG_HUMIDITY = "avg_humidity"
        const val CONDITION_TEXT = "condition_text"
        const val CONDITION_CODE = "condition_code"
        const val UV = "uv"
        const val DAY_WILL_IT_RAIN = "day_will_it_rain"
        const val DAY_CHANCE_OF_RAIN = "day_chance_of_rain"
        const val DAY_WILL_IT_SNOW = "day_will_it_snow"
        const val DAY_CHANCE_OF_SNOW = "day_chance_of_snow"
    }
}