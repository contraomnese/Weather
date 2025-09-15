package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.AlertNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity

fun AlertNetwork.toEntity(forecastLocationId: Int) = ForecastAlertEntity(
    forecastLocationId = forecastLocationId,
    headline = headline,
    msgType = msgType,
    severity = severity,
    urgency = urgency,
    areas = areas,
    category = category,
    certainty = certainty,
    event = event,
    note = note,
    effective = effective,
    expires = expires,
    desc = desc,
    instruction = instruction
)