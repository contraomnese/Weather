package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.dto.FavoriteDto
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorite: FavoriteEntity)

    @Query(
        "SELECT " +
                "locations.id AS ${FavoriteDto.ID}, " +
                "locations.name AS ${FavoriteDto.CITY_NAME}, " +
                "countries.name AS ${FavoriteDto.COUNTRY_NAME}, " +
                "locations.latitude AS ${FavoriteDto.LATITUDE}, " +
                "locations.longitude AS ${FavoriteDto.LONGITUDE} " +
                "FROM favorites " +
                "INNER JOIN locations ON favorites.location_id = locations.id " +
                "INNER JOIN countries ON locations.country_id = countries.id"
    )
    fun getFavorites(): List<FavoriteDto>

    @Query(
        "SELECT " +
                "locations.id AS ${FavoriteDto.ID}, " +
                "locations.name AS ${FavoriteDto.CITY_NAME}, " +
                "countries.name AS ${FavoriteDto.COUNTRY_NAME}, " +
                "locations.latitude AS ${FavoriteDto.LATITUDE}, " +
                "locations.longitude AS ${FavoriteDto.LONGITUDE} " +
                "FROM favorites " +
                "INNER JOIN locations ON favorites.location_id = locations.id " +
                "INNER JOIN countries ON locations.country_id = countries.id"
    )
    fun observeFavorites(): Flow<List<FavoriteDto>>

    @Query("DELETE FROM favorites WHERE location_id = :favoriteId")
    fun removeFavorite(favoriteId: Int)

}