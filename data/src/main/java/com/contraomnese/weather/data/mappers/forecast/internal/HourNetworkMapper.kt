package com.contraomnese.weather.data.mappers.forecast.internal

import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.HourlyForecastEntity
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import kotlin.math.roundToInt

private const val IS_DAY = 1

internal fun HourlyForecastEntity.toDomain(appSettings: AppSettings): ForecastHour {
    return ForecastHour(
        temperature = when (appSettings.temperatureUnit) {
            TemperatureUnit.Celsius -> tempC.roundToInt().toString()
            TemperatureUnit.Fahrenheit -> tempF.roundToInt().toString()
        },
        condition = CompactWeatherCondition.fromConditionCode(conditionCode),
        time = time.split(" ")[1],
        isDay = isDay == IS_DAY
    )
}

internal fun HourNetwork.toEntity(forecastDayId: Int) = HourlyForecastEntity(
    forecastDayId = forecastDayId,
    timeEpoch = timeEpoch,
    time = time,
    tempC = tempC,
    tempF = tempF,
    isDay = isDay,
    conditionText = condition.text,
    conditionCode = condition.code,
    windMph = windMph,
    windKph = windKph,
    windDegree = windDegree,
    windDir = windDir,
    pressureMb = pressureMb,
    pressureIn = pressureIn,
    precipMm = precipMm,
    precipIn = precipIn,
    snowCm = snowCm,
    humidity = humidity,
    cloud = cloud,
    feelsLikeC = feelsLikeC,
    feelsLikeF = feelsLikeF,
    windChillC = windChillC,
    windChillF = windChillF,
    heatIndexC = heatIndexC,
    heatIndexF = heatIndexF,
    dewPointC = dewPointC,
    dewPointF = dewPointF,
    willItRain = willItRain,
    chanceOfRain = chanceOfRain,
    willItSnow = willItSnow,
    chanceOfSnow = chanceOfSnow,
    visibilityKm = visibilityKm,
    visibilityMiles = visibilityMiles,
    gustMph = gustMph,
    gustKph = gustKph,
    uv = uv,
)