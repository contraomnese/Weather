package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity

data class ForecastData(
    @Embedded val location: ForecastLocationEntity,

    @Relation(
        entity = ForecastTodayEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastTodayEntity.FORECAST_LOCATION_ID
    )
    val todayForecast: ForecastTodayEntity,

    @Relation(
        entity = ForecastDailyEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastDailyEntity.FORECAST_LOCATION_ID
    )
    val dailyForecast: List<DailyForecastData>,

    @Relation(
        entity = ForecastAlertEntity::class,
        parentColumn = ForecastLocationEntity.ID,
        entityColumn = ForecastAlertEntity.FORECAST_LOCATION_ID
    )
    val alerts: List<ForecastAlertEntity>,
)