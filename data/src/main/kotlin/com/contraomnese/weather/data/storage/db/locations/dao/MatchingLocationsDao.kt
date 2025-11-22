package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

@Dao
interface MatchingLocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocations(locations: List<MatchingLocationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocation(location: MatchingLocationEntity)

    @Query("SELECT * FROM matching_locations WHERE network_id = :id")
    fun getLocation(id: Int): MatchingLocationEntity

    @Query("DELETE FROM matching_locations")
    fun removeAll()

}