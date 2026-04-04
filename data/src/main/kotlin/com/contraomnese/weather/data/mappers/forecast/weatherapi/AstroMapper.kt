package com.contraomnese.weather.data.mappers.forecast.weatherapi

import com.contraomnese.weather.data.network.models.weatherapi.AstroNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity

internal fun AstroNetwork.toEntity(forecastDayId: Int) = ForecastAstroEntity(
    forecastDailyId = forecastDayId,
    sunrise = this.sunrise,
    sunset = this.sunset,
    isSunUp = this.isSunUp
)