package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity

data class DailyForecastData(
    @Embedded
    val forecast: ForecastDailyEntity,

    @Relation(
        parentColumn = ForecastDailyEntity.ID,
        entityColumn = ForecastDayEntity.FORECAST_DAILY_ID
    )
    val day: ForecastDayEntity,

    @Relation(
        parentColumn = ForecastDailyEntity.ID,
        entityColumn = ForecastAstroEntity.FORECAST_DAILY_ID
    )
    val astro: ForecastAstroEntity,

    @Relation(
        parentColumn = ForecastDailyEntity.ID,
        entityColumn = ForecastHourEntity.FORECAST_DAILY_ID
    )
    val hour: List<ForecastHourEntity>,
)