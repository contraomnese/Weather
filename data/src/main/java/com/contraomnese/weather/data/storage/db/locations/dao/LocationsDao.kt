package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto
import com.contraomnese.weather.data.storage.db.locations.dto.LocationForecastDto

@Dao
interface LocationsDao {

    @Query(
        "SELECT " +
                "cities.id AS ${LocationDto.ID}, " +
                "cities.name AS ${LocationDto.NAME}, " +
                "cities.country_id AS ${LocationDto.COUNTRY_ID}, " +
                "countries.name AS ${LocationDto.COUNTRY_NAME} " +
                "FROM cities " +
                "INNER JOIN countries ON cities.country_id = countries.id " +
                "WHERE cities.name LIKE :cityName COLLATE NOCASE " +
                "LIMIT 10"
    )
    fun getLocationsBy(cityName: String): List<LocationDto>

    @Query(
        "SELECT " +
                "cities.id AS ${LocationForecastDto.ID}, " +
                "cities.name AS ${LocationForecastDto.NAME}, " +
                "cities.latitude AS ${LocationForecastDto.LATITUDE}, " +
                "cities.longitude AS ${LocationForecastDto.LONGITUDE} " +
                "FROM cities " +
                "WHERE cities.id LIKE :cityId " +
                "LIMIT 1"
    )
    fun getLocationBy(cityId: Int): LocationForecastDto
}