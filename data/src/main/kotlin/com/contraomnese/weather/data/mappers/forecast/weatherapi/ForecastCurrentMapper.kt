package com.contraomnese.weather.data.mappers.forecast.weatherapi

import com.contraomnese.weather.data.mappers.utils.kphToMph
import com.contraomnese.weather.data.mappers.utils.mmToInch
import com.contraomnese.weather.data.network.models.weatherapi.ForecastCurrentNetwork
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import kotlin.math.roundToInt

internal fun ForecastCurrentNetwork.toEntity(forecastLocationId: Int) = ForecastTodayEntity(
    forecastLocationId = forecastLocationId,
    tempC = tempC,
    tempF = tempF,
    isDay = isDay,
    conditionCode = condition.code,
    windMph = windMph,
    windKph = windKph,
    windDegree = windDegree,
    pressureMb = pressureMb,
    pressureIn = pressureIn,
    precipMm = precipMm,
    precipIn = precipIn,
    humidity = humidity,
    cloud = cloud,
    feelsLikeC = feelsLikeC,
    feelsLikeF = feelsLikeF,
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
    airQualityUSAIndex = null,
    airQualityUKIndex = forecastAirQuality.gbDefraIndex,
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
        WindSpeedUnit.Ms -> kphToMph(this.windKph).roundToInt().toString()
    }
}

internal fun ForecastTodayEntity.toGustDomain(windSpeedUnit: WindSpeedUnit): String {
    return when (windSpeedUnit) {
        WindSpeedUnit.Kph -> this.gustKph.roundToInt().toString()
        WindSpeedUnit.Mph -> this.gustMph.roundToInt().toString()
        WindSpeedUnit.Ms -> kphToMph(this.gustKph).roundToInt().toString()
    }
}

internal fun ForecastTodayEntity.toPressureDomain(pressureUnit: PressureUnit): Int {
    return when (pressureUnit) {
        PressureUnit.MmHg -> mmToInch(this.pressureIn).roundToInt()
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