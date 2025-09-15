package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = HourlyForecastEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastDayEntity::class,
        parentColumns = [ForecastDayEntity.ID],
        childColumns = [HourlyForecastEntity.FORECAST_DAY_ID],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class HourlyForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = FORECAST_DAY_ID) val forecastDayId: Int,
    @ColumnInfo(name = TIME_EPOCH) val timeEpoch: Long,
    @ColumnInfo(name = TIME) val time: String,
    @ColumnInfo(name = TEMP_C) val tempC: Double,
    @ColumnInfo(name = TEMP_F) val tempF: Double,
    @ColumnInfo(name = IS_DAY) val isDay: Int,
    @ColumnInfo(name = CONDITION_TEXT) val conditionText: String,
    @ColumnInfo(name = CONDITION_CODE) val conditionCode: Int,
    @ColumnInfo(name = WIND_MPH) val windMph: Double,
    @ColumnInfo(name = WIND_KPH) val windKph: Double,
    @ColumnInfo(name = WIND_DEGREE) val windDegree: Int,
    @ColumnInfo(name = WIND_DIR) val windDir: String,
    @ColumnInfo(name = PRESSURE_MB) val pressureMb: Double,
    @ColumnInfo(name = PRESSURE_IN) val pressureIn: Double,
    @ColumnInfo(name = PRECIP_MM) val precipMm: Double,
    @ColumnInfo(name = PRECIP_IN) val precipIn: Double,
    @ColumnInfo(name = SNOW_CM) val snowCm: Double,
    @ColumnInfo(name = HUMIDITY) val humidity: Int,
    @ColumnInfo(name = CLOUD) val cloud: Int,
    @ColumnInfo(name = FEELS_LIKE_C) val feelsLikeC: Double,
    @ColumnInfo(name = FEELS_LIKE_F) val feelsLikeF: Double,
    @ColumnInfo(name = WIND_CHILL_C) val windChillC: Double,
    @ColumnInfo(name = WIND_CHILL_F) val windChillF: Double,
    @ColumnInfo(name = HEAT_INDEX_C) val heatIndexC: Double,
    @ColumnInfo(name = HEAT_INDEX_F) val heatIndexF: Double,
    @ColumnInfo(name = DEW_POINT_C) val dewPointC: Double,
    @ColumnInfo(name = DEW_POINT_F) val dewPointF: Double,
    @ColumnInfo(name = WILL_IT_RAIN) val willItRain: Int,
    @ColumnInfo(name = CHANCE_OF_RAIN) val chanceOfRain: Int,
    @ColumnInfo(name = WILL_IT_SNOW) val willItSnow: Int,
    @ColumnInfo(name = CHANCE_OF_SNOW) val chanceOfSnow: Int,
    @ColumnInfo(name = VISIBILITY_KM) val visibilityKm: Double,
    @ColumnInfo(name = VISIBILITY_MILES) val visibilityMiles: Double,
    @ColumnInfo(name = GUST_MPH) val gustMph: Double,
    @ColumnInfo(name = GUST_KPH) val gustKph: Double,
    @ColumnInfo(name = UV) val uv: Double,
) {
    companion object {
        const val TABLE_NAME = "hourly_forecast"
        const val FORECAST_DAY_ID = "forecast_day_id"
        const val TIME_EPOCH = "time_epoch"
        const val TIME = "time"
        const val TEMP_C = "temp_c"
        const val TEMP_F = "temp_f"
        const val IS_DAY = "is_day"
        const val CONDITION_TEXT = "condition_text"
        const val CONDITION_CODE = "condition_code"
        const val WIND_MPH = "wind_mph"
        const val WIND_KPH = "wind_kph"
        const val WIND_DEGREE = "wind_degree"
        const val WIND_DIR = "wind_dir"
        const val PRESSURE_MB = "pressure_mb"
        const val PRESSURE_IN = "pressure_in"
        const val PRECIP_MM = "precip_mm"
        const val PRECIP_IN = "precip_in"
        const val SNOW_CM = "snow_cm"
        const val HUMIDITY = "humidity"
        const val CLOUD = "cloud"
        const val FEELS_LIKE_C = "feels_like_c"
        const val FEELS_LIKE_F = "feels_like_f"
        const val WIND_CHILL_C = "wind_chill_c"
        const val WIND_CHILL_F = "wind_chill_f"
        const val HEAT_INDEX_C = "heat_index_c"
        const val HEAT_INDEX_F = "heat_index_f"
        const val DEW_POINT_C = "dew_point_c"
        const val DEW_POINT_F = "dew_point_f"
        const val WILL_IT_RAIN = "will_it_rain"
        const val CHANCE_OF_RAIN = "chance_of_rain"
        const val WILL_IT_SNOW = "will_it_snow"
        const val CHANCE_OF_SNOW = "chance_of_snow"
        const val VISIBILITY_KM = "visibility_km"
        const val VISIBILITY_MILES = "visibility_miles"
        const val GUST_MPH = "gust_mph"
        const val GUST_KPH = "gust_kph"
        const val UV = "uv"
    }
}