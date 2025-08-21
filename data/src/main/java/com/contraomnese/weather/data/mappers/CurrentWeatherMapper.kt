package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.CurrentWeatherResponse
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherDomainModel
import kotlin.math.roundToInt

fun CurrentWeatherResponse.toDomain(): WeatherDomainModel {
    return WeatherDomainModel(
        currentTemperature = current.tempC.roundToInt().toString(),
        condition = current.condition.text,
    )
}