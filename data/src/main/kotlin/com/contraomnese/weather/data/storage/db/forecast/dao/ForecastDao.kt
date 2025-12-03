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
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastData
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

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
}