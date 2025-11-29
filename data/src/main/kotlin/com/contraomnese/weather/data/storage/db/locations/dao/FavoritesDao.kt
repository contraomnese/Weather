package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)


    @Query(
        "INSERT INTO favorites (location_id, city, state, country, latitude, longitude) " +
                "SELECT network_id, city, state, country, latitude, longitude " +
                "FROM matching_locations WHERE network_id = :locationId"
    )
    suspend fun addFavorite(locationId: Int)

    @Query("SELECT * FROM favorites")
    suspend fun getFavorites(): List<FavoriteEntity>

    @Query("SELECT * FROM favorites")
    fun observeFavorites(): Flow<List<FavoriteEntity>>

    @Query("DELETE FROM favorites WHERE location_id = :favoriteId")
    suspend fun removeFavorite(favoriteId: Int)

}