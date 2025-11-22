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
import com.contraomnese.weather.data.storage.db.forecast.entities.DayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastCurrentEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.HourlyForecastEntity
import kotlinx.coroutines.flow.Flow

data class LocationWithForecasts(
    @Embedded val location: ForecastLocationEntity,

    @Relation(
        entity = ForecastCurrentEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastCurrentEntity.FORECAST_LOCATION_ID
    )
    val forecastCurrent: ForecastCurrentEntity,

    @Relation(
        entity = ForecastDayEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastDayEntity.FORECAST_LOCATION_ID
    )
    val forecastDays: List<ForecastDayWithDetails>,

    @Relation(
        entity = ForecastAlertEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastAlertEntity.FORECAST_LOCATION_ID
    )
    val forecastAlert: List<ForecastAlertEntity>,

    )

data class ForecastDayWithDetails(
    @Embedded
    val forecast: ForecastDayEntity,

    @Relation(
        parentColumn = ForecastDayEntity.ID,
        entityColumn = DayEntity.FORECAST_DAY_ID
    )
    val day: DayEntity,

    @Relation(
        parentColumn = ForecastDayEntity.ID,
        entityColumn = ForecastAstroEntity.FORECAST_DAY_ID
    )
    val astro: ForecastAstroEntity,

    @Relation(
        parentColumn = ForecastDayEntity.ID,
        entityColumn = HourlyForecastEntity.FORECAST_DAY_ID
    )
    val hour: List<HourlyForecastEntity>,
)

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastLocation(location: ForecastLocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastCurrent(current: ForecastCurrentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastDay(day: ForecastDayEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: DayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAstro(astro: ForecastAstroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hours: List<HourlyForecastEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<ForecastAlertEntity>)

    @Transaction
    @Query("SELECT * FROM forecast_location WHERE location_id = :locationId")
    suspend fun getForecastBy(locationId: Int): LocationWithForecasts?

    @Transaction
    @Query("SELECT * FROM forecast_location WHERE location_id = :locationId")
    fun observeForecastBy(locationId: Int): Flow<LocationWithForecasts?>

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