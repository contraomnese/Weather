package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto

@Dao
interface LocationsDao {

    @Query(
        "SELECT " +
                "cities.id AS ${LocationDto.ID}, " +
                "cities.name AS ${LocationDto.NAME}, " +
                "cities.country_id AS ${LocationDto.COUNTRY_ID}, " +
                "countries.name AS ${LocationDto.COUNTRY_NAME}, " +
                "cities.latitude AS ${LocationDto.LATITUDE}, " +
                "cities.longitude AS ${LocationDto.LONGITUDE} " +
                "FROM cities " +
                "INNER JOIN countries ON cities.country_id = countries.id " +
                "WHERE cities.name LIKE :cityName COLLATE NOCASE " +
                "LIMIT 10"
    )
    fun getLocations(cityName: String): List<LocationDto>
}