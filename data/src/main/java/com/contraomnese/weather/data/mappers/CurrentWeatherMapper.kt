package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.CurrentWeatherResponse
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentWeatherDomainModel

fun CurrentWeatherResponse.toDomain(): CurrentWeatherDomainModel {
    return CurrentWeatherDomainModel(currentTemperature = current.tempC.toString())
}