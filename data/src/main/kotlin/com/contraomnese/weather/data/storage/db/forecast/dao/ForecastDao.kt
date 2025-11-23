package com.contraomnese.weather.data.storage.db.forecast.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.contraomnese.weather.data.mappers.forecast.internal.toEntity
import com.contraomnese.weather.data.mappers.forecast.internal.toForecastDayEntity
import com.contraomnese.weather.data.mappers.location.toEntity
import com.contraomnese.weather.data.network.models.ForecastResponse
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import kotlinx.coroutines.flow.Flow

data class ForecastData(
    @Embedded val location: ForecastLocationEntity,

    @Relation(
        entity = ForecastTodayEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastTodayEntity.FORECAST_LOCATION_ID
    )
    val todayForecast: ForecastTodayEntity,

    @Relation(
        entity = ForecastDailyEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastDailyEntity.FORECAST_LOCATION_ID
    )
    val dailyForecast: List<DailyForecastData>,

    @Relation(
        entity = ForecastAlertEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastAlertEntity.FORECAST_LOCATION_ID
    )
    val alerts: List<ForecastAlertEntity>,
)

data class DailyForecastData(
    @Embedded
    val forecast: ForecastDailyEntity,

    @Relation(
        parentColumn = ForecastDailyEntity.ID,
        entityColumn = ForecastDayEntity.FORECAST_DAILY_ID
    )
    val day: ForecastDayEntity,

    @Relation(
        parentColumn = ForecastDailyEntity.ID,
        entityColumn = ForecastAstroEntity.FORECAST_DAILY_ID
    )
    val astro: ForecastAstroEntity,

    @Relation(
        parentColumn = ForecastDailyEntity.ID,
        entityColumn = ForecastHourEntity.FORECAST_DAILY_ID
    )
    val hour: List<ForecastHourEntity>,
)

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastLocation(location: ForecastLocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastCurrent(current: ForecastTodayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastDay(day: ForecastDailyEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: ForecastDayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstro(astro: ForecastAstroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hours: List<ForecastHourEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<ForecastAlertEntity>)

    @Transaction
    @Query("SELECT * FROM forecast_location WHERE location_id = :locationId")
    suspend fun getForecastBy(locationId: Int): ForecastData?

    @Transaction
    @Query("SELECT * FROM forecast_location WHERE location_id = :locationId")
    fun observeForecastBy(locationId: Int): Flow<ForecastData?>

    @Query("DELETE FROM forecast_location WHERE location_id = :locationId")
    suspend fun deleteForecastForLocation(locationId: Int)

    @Transaction
    suspend fun updateForecastForLocation(
        locationId: Int,
        locationName: String,
        forecastResponse: ForecastResponse,
    ) {

        deleteForecastForLocation(locationId)
        val forecastLocationId = insertForecastLocation(forecastResponse.location.toEntity(locationId).copy(name = locationName)).toInt()

        insertForecastCurrent(forecastResponse.current.toEntity(forecastLocationId))
        insertAlerts(forecastResponse.alerts.alert.map { it.toEntity(forecastLocationId) })

        forecastResponse.forecast.forecastDay.forEach { forecast ->
            val forecastDayId = insertForecastDay(forecast.toForecastDayEntity(forecastLocationId)).toInt()
            insertDay(forecast.toEntity(forecastDayId))
            insertAstro(forecast.astro.toEntity(forecastDayId))
            insertHourlyForecast(forecast.hour.map { it.toEntity(forecastDayId) })
        }
    }
}