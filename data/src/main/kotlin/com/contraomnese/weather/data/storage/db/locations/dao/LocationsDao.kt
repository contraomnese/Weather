package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatchingLocations(locations: List<MatchingLocationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastLocation(location: ForecastLocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatchingLocation(location: MatchingLocationEntity)

    @Query("SELECT * FROM matching_locations WHERE network_id = :id")
    fun getMatchingLocation(id: Int): MatchingLocationEntity

    @Query("DELETE FROM matching_locations")
    fun deleteAllMatchingLocations()

}