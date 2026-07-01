package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.ColumnInfo
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity

data class LastUpdatedData(
    @ColumnInfo(name = ForecastLocationEntity.LAST_UPDATED_TIME)
    val lastUpdatedTime: Long,

    @ColumnInfo(name = ForecastLocationEntity.TIME_ZONE_ID)
    val timeZoneId: String,
)
