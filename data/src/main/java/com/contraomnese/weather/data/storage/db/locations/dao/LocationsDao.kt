package com.contraomnese.weather.data.storage.db.locations.dao

import androidx.room.Dao
import androidx.room.Query
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastLocationDto
import com.contraomnese.weather.data.storage.db.locations.dto.LocationDto

@Dao
interface LocationsDao {

    @Query(
        """
    SELECT 
        locations.id AS ${LocationDto.ID},
        locations.name AS ${LocationDto.NAME},
        locations.country_id AS ${LocationDto.COUNTRY_ID},
        countries.name AS ${LocationDto.COUNTRY_NAME},
        CASE WHEN favorites.id IS NOT NULL THEN 1 ELSE 0 END AS ${LocationDto.IS_FAVORITE}
    FROM locations
    INNER JOIN countries ON locations.country_id = countries.id
    LEFT JOIN favorites ON favorites.location_id = locations.id
    WHERE locations.name LIKE :cityName COLLATE NOCASE
    LIMIT 10
    """
    )
    fun getLocationsBy(cityName: String): List<LocationDto>

    @Query(
        "SELECT " +
                "locations.id AS ${ForecastLocationDto.ID}, " +
                "locations.name AS ${ForecastLocationDto.NAME}, " +
                "locations.latitude AS ${ForecastLocationDto.LATITUDE}, " +
                "locations.longitude AS ${ForecastLocationDto.LONGITUDE} " +
                "FROM locations " +
                "WHERE locations.id LIKE :cityId " +
                "LIMIT 1"
    )
    fun getLocationBy(cityId: Int): ForecastLocationDto
}