package com.contraomnese.weather.data.storage.db.forecast.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = ForecastCurrentEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = ForecastLocationEntity::class,
        parentColumns = [ForecastLocationEntity.ID],
        childColumns = [ForecastCurrentEntity.FORECAST_LOCATION_ID],
        onDelete = ForeignKey.CASCADE,
    )],
)
data class ForecastCurrentEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = FORECAST_LOCATION_ID) val forecastLocationId: Int,
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
    @ColumnInfo(name = HUMIDITY) val humidity: Int,
    @ColumnInfo(name = CLOUD) val cloud: Int,
    @ColumnInfo(name = FEELS_LIKE_C) val feelsLikeC: Double,
    @ColumnInfo(name = FEELS_LIKE_F) val feelsLikeF: Double,
    @ColumnInfo(name = WIND_CHILL_C) val windChillC: Double,
    @ColumnInfo(name = WIND_CHILL_F) val windChillF: Double,
    @ColumnInfo(name = HEAT_INDEX_C) val heatIndexC: Double,
    @ColumnInfo(name = HEAT_INDEX_F) val heatIndexF: Double,
    @ColumnInfo(name = DEW_POINT_C) val dewPointC: Float,
    @ColumnInfo(name = DEW_POINT_F) val dewPointF: Float,
    @ColumnInfo(name = VISIBILITY_KM) val visibilityKm: Double,
    @ColumnInfo(name = VISIBILITY_MILES) val visibilityMiles: Double,
    @ColumnInfo(name = UV) val uv: Double,
    @ColumnInfo(name = GUST_MPH) val gustMph: Double,
    @ColumnInfo(name = GUST_KPH) val gustKph: Double,
    @ColumnInfo(name = AIR_QUALITY_CO) val airQualityCo: Float,
    @ColumnInfo(name = AIR_QUALITY_NO2) val airQualityNo2: Float,
    @ColumnInfo(name = AIR_QUALITY_O3) val airQualityO3: Float,
    @ColumnInfo(name = AIR_QUALITY_SO2) val airQualitySo2: Float,
    @ColumnInfo(name = AIR_QUALITY_PM25) val airQualityPm25: Float,
    @ColumnInfo(name = AIR_QUALITY_PM10) val airQualityPm10: Float,
    @ColumnInfo(name = AIR_QUALITY_US_EPA_INDEX) val airQualityUsEpaIndex: Int,
    @ColumnInfo(name = AIR_QUALITY_GB_DEFRA_INDEX) val airQualityGbDefraIndex: Int,
    @ColumnInfo(name = LAST_UPDATED_EPOCH) val lastUpdatedEpoch: Long,
) {
    companion object {
        const val TABLE_NAME = "forecast_current"
        const val FORECAST_LOCATION_ID = "forecast_location_id"
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
        const val VISIBILITY_KM = "visibility_km"
        const val VISIBILITY_MILES = "visibility_miles"
        const val UV = "uv"
        const val GUST_MPH = "gust_mph"
        const val GUST_KPH = "gust_kph"
        const val AIR_QUALITY_CO = "air_quality_co"
        const val AIR_QUALITY_NO2 = "air_quality_no2"
        const val AIR_QUALITY_O3 = "air_quality_o3"
        const val AIR_QUALITY_SO2 = "air_quality_so2"
        const val AIR_QUALITY_PM25 = "air_quality_pm25"
        const val AIR_QUALITY_PM10 = "air_quality_pm10"
        const val AIR_QUALITY_US_EPA_INDEX = "air_quality_us_epa_index"
        const val AIR_QUALITY_GB_DEFRA_INDEX = "air_quality_gb_defra_index"
        const val LAST_UPDATED_EPOCH = "last_updated_epoch"
    }
}