package com.contraomnese.weather.data.mappers

import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.network.models.ForecastWeatherResponse
import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsInfo
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastInfo
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfo
import kotlin.math.roundToInt

fun ForecastWeatherResponse.toDomain(): ForecastWeatherDomainModel {
    return ForecastWeatherDomainModel(
        locationInfo = LocationInfo(
            locationTimeEpoch = location.localtimeEpoch,
            locationTime = location.localtime
        ),
        currentInfo = CurrentInfo(
            temperatureC = current.tempC.roundToInt().toString(),
            temperatureF = current.tempF.roundToInt().toString(),
            feelsLikeC = current.feelsLikeC.roundToInt().toString(),
            feelsLikeF = current.feelsLikeF.roundToInt().toString(),
            isDay = current.isDay == 1,
            conditionCode = current.condition.code,
            conditionText = current.condition.text,
            windSpeedKph = current.windKph.roundToInt().toString(),
            windSpeedMph = current.windMph.roundToInt().toString(),
            windDirection = current.windDir,
            windDegree = current.windDegree,
            pressureMb = current.pressureMb.toString(),
            pressureIn = current.pressureIn.toString(),
            humidity = current.humidity.toString(),
            uvIndex = current.uv.toString(),
            airQualityIndex = current.airQuality.usEpaIndex
        ),
        forecastInfo = ForecastInfo(
            today = forecast.forecastDay.first().toForecastTodayDomain(),
            forecastDays = forecast.forecastDay.map { it.toForecastDayDomain() },
            forecastHours = forecast.forecastDay.first().hour.filter { it.timeEpoch > location.localtimeEpoch }
                .map { it.toDomain() }
        ),
        alertsInfo = AlertsInfo(
            alerts = alerts.alert.map { it.desc }
        )
    )
}

fun ForecastDayNetwork.toForecastTodayDomain(): ForecastToday {

    return ForecastToday(
        maxTemperatureC = day.maxTempC.roundToInt().toString(),
        maxTemperatureF = day.maxTempF.roundToInt().toString(),
        minTemperatureC = day.minTempC.roundToInt().toString(),
        minTemperatureF = day.minTempF.roundToInt().toString(),
        conditionCode = day.condition.code,
        conditionText = day.condition.text,
        totalUvIndex = day.uv.toString(),
        rainChance = day.dailyChanceOfRain.toString(),
        totalRainFullMm = day.totalPrecipMm.roundToInt().toString(),
        sunrise = astro.sunrise,
        sunset = astro.sunset
    )
}

fun ForecastDayNetwork.toForecastDayDomain(): ForecastDay {
    return ForecastDay(
        maxTemperatureC = day.maxTempC.roundToInt().toString(),
        maxTemperatureF = day.maxTempF.roundToInt().toString(),
        minTemperatureC = day.minTempC.roundToInt().toString(),
        minTemperatureF = day.minTempF.roundToInt().toString(),
        conditionCode = day.condition.code,
        totalRainFullMm = day.totalPrecipMm.roundToInt(),
    )
}

fun HourNetwork.toDomain(): ForecastHour {
    return ForecastHour(
        temperatureC = tempC.roundToInt().toString(),
        temperatureF = tempF.roundToInt().toString(),
        conditionCode = condition.code,
        time = time.split(" ")[1],
    )
}

