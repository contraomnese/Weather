package com.contraomnese.weather.data.mappers.forecast.internal

import com.contraomnese.weather.data.network.models.ForecastCurrentNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import com.contraomnese.weather.data.utils.toMs
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import kotlin.math.roundToInt

private const val MM_IN_INCH = 25.4

internal fun ForecastCurrentNetwork.toEntity(forecastLocationId: Int) = ForecastTodayEntity(
    forecastLocationId = forecastLocationId,
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
    visibilityKm = visibilityKm,
    visibilityMiles = visibilityMiles,
    uv = uv,
    gustMph = gustMph,
    gustKph = gustKph,
    airQualityCo = forecastAirQuality.co,
    airQualityPm10 = forecastAirQuality.pm10,
    airQualityPm25 = forecastAirQuality.pm25,
    airQualitySo2 = forecastAirQuality.so2,
    airQualityO3 = forecastAirQuality.o3,
    airQualityNo2 = forecastAirQuality.no2,
    airQualityUsEpaIndex = forecastAirQuality.usEpaIndex,
    airQualityGbDefraIndex = forecastAirQuality.gbDefraIndex,
    lastUpdatedEpoch = lastUpdatedEpoch,
)

internal fun ForecastTodayEntity.toTemperatureDomain(temperatureUnit: TemperatureUnit): String {
    return when (temperatureUnit) {
        TemperatureUnit.Celsius -> this.tempC.roundToInt().toString()
        TemperatureUnit.Fahrenheit -> this.tempF.roundToInt().toString()
    }
}

internal fun ForecastTodayEntity.toWindDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.windKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.windMph.roundToInt().toString()
        WindSpeedUnit.Ms -> this.windKph.toMs().roundToInt().toString()
    }
}

internal fun ForecastTodayEntity.toGustDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.gustKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.gustMph.roundToInt().toString()
        WindSpeedUnit.Ms -> this.gustKph.toMs().roundToInt().toString()
    }
}

internal fun ForecastTodayEntity.toPressureDomain(pressureUnit: PressureUnit): Int {
    return when (pressureUnit) {
        PressureUnit.MmHg -> (this.pressureIn * MM_IN_INCH).roundToInt()
        PressureUnit.GPa -> this.pressureMb.roundToInt()
    }
}

internal fun ForecastTodayEntity.toDewPoint(temperatureUnit: TemperatureUnit): Int {
    return when (temperatureUnit) {
        TemperatureUnit.Celsius -> this.dewPointC.roundToInt()
        TemperatureUnit.Fahrenheit -> this.dewPointF.roundToInt()
    }
}

internal fun ForecastHourEntity.toPrecipitationDomain(precipitationUnit: PrecipitationUnit): Double {
    return when (precipitationUnit) {
        PrecipitationUnit.Millimeters -> this.precipMm
        PrecipitationUnit.Inches -> this.precipIn
    }
}