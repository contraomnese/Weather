package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import kotlin.math.roundToInt

private const val IS_DAY = 1

fun HourNetwork.toDomain(appSettings: AppSettings): ForecastHour {
    return ForecastHour(
        temperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> tempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> tempF.roundToInt().toString()
        },
        conditionCode = condition.code,
        time = time.split(" ")[1],
        isDay = isDay == IS_DAY
    )
}