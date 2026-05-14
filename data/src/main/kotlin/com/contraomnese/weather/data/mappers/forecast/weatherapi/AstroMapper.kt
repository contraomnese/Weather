package com.contraomnese.weather.data.mappers.forecast.weatherapi

import com.contraomnese.weather.data.network.models.weatherapi.AstroNetwork
import com.contraomnese.weather.data.parsers.DateTimeParser
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal fun AstroNetwork.toEntity(forecastDayId: Int, timeZone: TimeZone): ForecastAstroEntity {

    val now = Clock.System.now().toLocalDateTime(timeZone).toInstant(timeZone).epochSeconds

    val sunrise = DateTimeParser.parseAmPmTime(this.sunrise).time.toSecondOfDay() + now
    val sunset = DateTimeParser.parseAmPmTime(this.sunset).time.toSecondOfDay() + now

    return ForecastAstroEntity(
        forecastDailyId = forecastDayId,
        sunrise = sunrise,
        sunset = sunset,
        isSunUp = this.isSunUp
    )
}