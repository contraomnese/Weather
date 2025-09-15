package com.contraomnese.weather.data.storage.db.locations.dto

import androidx.room.ColumnInfo
import com.contraomnese.weather.data.storage.db.forecast.entities.DayEntity

//@DatabaseView(
//    viewName = "day_dto",
//    value = """
//        SELECT
//            forecast_day_id AS ${DayEntity.FORECAST_DAY_ID},
//            max_temp_c AS ${DayEntity.MAX_TEMP_C},
//            min_temp_c AS ${DayEntity.MIN_TEMP_C},
//            max_temp_f AS ${DayEntity.MAX_TEMP_F},
//            min_temp_f AS ${DayEntity.MIN_TEMP_F},
//            condition_text AS ${DayEntity.CONDITION_TEXT},
//            condition_code AS ${DayEntity.CONDITION_CODE},
//            uv AS ${DayEntity.UV},
//            day_chance_of_rain AS ${DayEntity.DAY_CHANCE_OF_RAIN},
//            total_precip_mm AS ${DayEntity.TOTAL_PRECIP_MM},
//            total_precip_in AS ${DayEntity.TOTAL_PRECIP_IN},
//            day_will_it_rain AS ${DayEntity.DAY_WILL_IT_RAIN}
//        FROM day
//    """
//)
data class DayDto(
    @ColumnInfo(name = DayEntity.FORECAST_DAY_ID) val forecastDayId: Int = 0,
    @ColumnInfo(name = DayEntity.MAX_TEMP_C) val maxTempC: Double,
    @ColumnInfo(name = DayEntity.MIN_TEMP_C) val minTempC: Double,
    @ColumnInfo(name = DayEntity.MAX_TEMP_F) val maxTempF: Double,
    @ColumnInfo(name = DayEntity.MIN_TEMP_F) val minTempF: Double,
    @ColumnInfo(name = DayEntity.CONDITION_TEXT) val conditionText: String,
    @ColumnInfo(name = DayEntity.CONDITION_CODE) val conditionCode: Int,
    @ColumnInfo(name = DayEntity.UV) val uv: Double,
    @ColumnInfo(name = DayEntity.DAY_CHANCE_OF_RAIN) val dailyChanceOfRain: Int,
    @ColumnInfo(name = DayEntity.TOTAL_PRECIP_MM) val totalPrecipMm: Double,
    @ColumnInfo(name = DayEntity.TOTAL_PRECIP_IN) val totalPrecipIn: Double,
    @ColumnInfo(name = DayEntity.DAY_WILL_IT_RAIN) val dailyWillItRain: Int,
)
