package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherDomainModel
import kotlin.math.roundToInt

fun ForecastWeatherResponse.toDomain(): WeatherDomainModel {
    return WeatherDomainModel(
        currentTemperature = current.tempC.roundToInt().toString(),
        condition = current.condition.text,
        maxTemperature = forecast.forecastDay.first().day.maxTempC.roundToInt().toString(),
        minTemperature = forecast.forecastDay.first().day.minTempC.roundToInt().toString()
    )
}