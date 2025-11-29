package com.contraomnese.weather.data.storage.db.forecast.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastData
import kotlinx.coroutines.flow.Flow

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
    suspend fun deleteForecastLocation(locationId: Int)

//    @Transaction
//    suspend fun updateForecast(
//        forecastResponse: ForecastResponse,
//        forecastLocationEntity: ForecastLocationEntity
//    ) {
//
//        deleteForecastLocation(forecastLocationEntity.locationId)
//        val forecastLocationId = insertForecastLocation(forecastLocationEntity).toInt()
//
//        insertForecastCurrent(forecastResponse.current.toEntity(forecastLocationId))
//        insertAlerts(forecastResponse.alerts.alert.map { it.toEntity(forecastLocationId) })
//
//        forecastResponse.forecast.forecastDay.forEach { forecast ->
//            val forecastDayId = insertForecastDay(forecast.toForecastDayEntity(forecastLocationId)).toInt()
//            insertDay(forecast.toEntity(forecastDayId))
//            insertAstro(forecast.astro.toEntity(forecastDayId))
//            insertHourlyForecast(forecast.hour.map { it.toEntity(forecastDayId) })
//        }
//    }
}