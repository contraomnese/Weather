package com.contraomnese.weather.data.mappers.forecast.internal

import com.contraomnese.weather.data.network.models.AstroNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity

internal fun AstroNetwork.toEntity(forecastDayId: Int) = ForecastAstroEntity(
    forecastDayId = forecastDayId,
    sunrise = this.sunrise,
    sunset = this.sunset,
    moonrise = this.moonrise,
    moonset = this.moonset,
    moonPhase = this.moonPhase,
    moonIllumination = this.moonIllumination,
    isMoonUp = this.isMoonUp,
    isSunUp = this.isSunUp
)