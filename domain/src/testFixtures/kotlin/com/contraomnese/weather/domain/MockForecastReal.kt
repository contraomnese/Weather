package com.contraomnese.weather.domain

import com.contraomnese.weather.domain.weatherByLocation.model.AirQuality
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsWeather
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastLocation
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeather
import com.contraomnese.weather.domain.weatherByLocation.model.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.LocationTime
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel
import com.contraomnese.weather.domain.weatherByLocation.model.UvIndex
import com.contraomnese.weather.domain.weatherByLocation.model.Weather
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

object MockForecastReal {
    fun take() = Forecast(
        alerts = AlertsWeather(
            alerts = listOf(
                "Порывы до 15",
                "Ожидается усиление ветра в порывах до 15 м/с",
                "На дорогах ожидается гололедица.",
                "На дорогах ожидается гололедица.",
                "На дорогах местами ожидается гололедица.",
                "На дорогах местами ожидается гололедица."
            ).toImmutableList()
        ),
        forecast = ForecastWeather(
            days = listOf(
                ForecastDay(
                    condition = WeatherCondition.RAIN,
                    dayName = "Mon",
                    dayNumber = "24.11",
                    maxTemperature = 5,
                    minTemperature = 0,
                    totalRainFull = 3
                ),
                ForecastDay(
                    condition = WeatherCondition.SNOW,
                    dayName = "Tue",
                    dayNumber = "25.11",
                    maxTemperature = 1,
                    minTemperature = 0,
                    totalRainFull = 1
                ),
                ForecastDay(
                    condition = WeatherCondition.RAIN,
                    dayName = "Wed",
                    dayNumber = "26.11",
                    maxTemperature = 2,
                    minTemperature = 1,
                    totalRainFull = 4
                )
            ).toImmutableList(),
            hours = listOf(
                ForecastHour(condition = WeatherCondition.PARTLY_CLOUDY, isDay = false, temperature = "3", time = "17:00"),
                ForecastHour(condition = WeatherCondition.RAIN, isDay = false, temperature = "2", time = "18:00"),
                ForecastHour(condition = WeatherCondition.RAIN, isDay = false, temperature = "1", time = "19:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "1", time = "20:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "1", time = "21:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "2", time = "22:00"),
                ForecastHour(condition = WeatherCondition.SLEET, isDay = false, temperature = "1", time = "23:00"),
                ForecastHour(condition = WeatherCondition.SLEET, isDay = false, temperature = "1", time = "00:00"),
                ForecastHour(condition = WeatherCondition.SNOW, isDay = false, temperature = "0", time = "01:00"),
                ForecastHour(condition = WeatherCondition.SLEET, isDay = false, temperature = "0", time = "02:00"),
                ForecastHour(condition = WeatherCondition.SNOW, isDay = false, temperature = "0", time = "03:00"),
                ForecastHour(condition = WeatherCondition.SLEET, isDay = false, temperature = "0", time = "04:00"),
                ForecastHour(condition = WeatherCondition.SLEET, isDay = false, temperature = "0", time = "05:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "0", time = "06:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "0", time = "07:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "0", time = "08:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = true, temperature = "0", time = "09:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = true, temperature = "0", time = "10:00"),
                ForecastHour(condition = WeatherCondition.PARTLY_CLOUDY, isDay = true, temperature = "0", time = "11:00"),
                ForecastHour(condition = WeatherCondition.PARTLY_CLOUDY, isDay = true, temperature = "1", time = "12:00"),
                ForecastHour(condition = WeatherCondition.PARTLY_CLOUDY, isDay = true, temperature = "1", time = "13:00"),
                ForecastHour(condition = WeatherCondition.PARTLY_CLOUDY, isDay = true, temperature = "1", time = "14:00"),
                ForecastHour(condition = WeatherCondition.PARTLY_CLOUDY, isDay = true, temperature = "0", time = "15:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = true, temperature = "0", time = "16:00"),
                ForecastHour(condition = WeatherCondition.CLOUDY, isDay = false, temperature = "0", time = "17:00")
            ).toImmutableList(),
            today = ForecastToday(
                conditionCode = 1063,
                conditionText = "Patchy rain nearby",
                maxTemperature = "5",
                minTemperature = "0",
                rainChance = "97",
                sunrise = LocationTime(time = LocalTime(hour = 8, minute = 23)),
                sunset = LocationTime(time = LocalTime(hour = 16, minute = 9)),
                totalRainFull = "3",
                totalUvIndex = "0.0"
            )
        ),
        location = ForecastLocation(
            city = "Moscow",
            country = "Russia",
            isSunUp = false,
            latitude = 55.752,
            localTime = LocationDateTime(LocalDateTime(2025, 11, 24, 17, 3)),
            localTimeEpoch = 1763993017L,
            longitude = 37.616,
            timeZone = TimeZone.of("Europe/Moscow")
        ),
        today = Weather(
            airQuality = AirQuality(
                aqiIndex = 7,
                aqiText = "High",
                coLevel = PollutantLevel.Good,
                no2Level = PollutantLevel.Good,
                o3Level = PollutantLevel.Good,
                pm10Level = PollutantLevel.Moderate,
                pm25Level = PollutantLevel.Bad,
                so2Level = PollutantLevel.Moderate
            ),
            condition = WeatherCondition.PARTLY_CLOUDY,
            conditionText = "Partly cloudy",
            dewPoint = 0,
            feelsLikeTemperature = "-2",
            gustSpeed = "31",
            humidity = 87,
            isDay = false,
            isRainingExpected = true,
            maxRainfall = 0.98,
            pressure = 752,
            rainfallAfterNow = listOf(
                0.01,
                0.01,
                0.0,
                0.0,
                0.0,
                0.01,
                0.01,
                0.02,
                0.01,
                0.02,
                0.01,
                0.01,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
            ).toImmutableList(),
            rainfallBeforeNow = listOf(
                0.01,
                0.17,
                0.0,
                0.58,
                0.2,
                0.09,
                0.0,
                0.0,
                0.0,
                0.0,
                0.29,
                0.98,
                0.35,
                0.29,
                0.03,
                0.01,
                0.0,
                0.0
            ).toImmutableList(),
            rainfallNow = 0.01,
            temperature = "3",
            uvIndex = UvIndex(0),
            windDegree = 248,
            windDirection = "ЗЮЗ",
            windSpeed = "23"
        )
    )
}