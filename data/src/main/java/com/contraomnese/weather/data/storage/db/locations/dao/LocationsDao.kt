package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.dto.CityDto

@Dao
interface LocationsDao {

    @Query(
        "SELECT id as ${CityDto.ID}, " +
                "name as ${CityDto.NAME}, " +
                "country_id as ${CityDto.COUNTRY_ID}, " +
                "latitude as ${CityDto.LATITUDE}, " +
                "longitude as ${CityDto.LONGITUDE} " +
                "FROM cities " +
                "WHERE name LIKE :cityName COLLATE NOCASE " +
                "LIMIT 10"
    )
    fun getLocations(cityName: String): List<CityDto>
}