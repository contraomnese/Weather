package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity

data class FavoriteForecastData(
    @Embedded val location: ForecastLocationEntity,

    @Relation(
        entity = ForecastTodayEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastTodayEntity.FORECAST_LOCATION_ID
    )
    val todayForecast: ForecastTodayEntity,
)