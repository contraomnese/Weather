package com.contraomnese.weather.domain

import com.contraomnese.weather.domain.weatherByLocation.model.AirQuality
import com.contraomnese.weather.domain.weatherByLocation.model.AlertsWeather
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastLocation
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastToday
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeather
import com.contraomnese.weather.domain.weatherByLocation.model.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel
import com.contraomnese.weather.domain.weatherByLocation.model.UvIndex
import com.contraomnese.weather.domain.weatherByLocation.model.Weather
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.random.Random

object ForecastDomainFixtures {

    private val random = Random(1234)
    private val levels = listOf(PollutantLevel.Bad, PollutantLevel.Moderate, PollutantLevel.Good)
    private val conditions = listOf(CompactWeatherCondition.CLEAR, CompactWeatherCondition.CLOUDY, CompactWeatherCondition.RAIN)
    private val directions = listOf("N", "S", "E", "W")

    fun generate(): Forecast {
        return Forecast(
            location = randomLocation(),
            today = randomWeather(),
            forecast = randomForecastWeather(),
            alerts = AlertsWeather(
                alerts = persistentListOf(
                    if (random.nextBoolean()) "Storm warning" else "None"
                )
            )
        )
    }

    private fun randomLocation(): ForecastLocation {
        val city = listOf("London", "Berlin", "Tokyo", "Oslo").random(random)
        return ForecastLocation(
            city = city,
            country = listOf("UK", "Germany", "Japan", "Norway").random(random),
            latitude = random.nextDouble(-90.0, 90.0),
            longitude = random.nextDouble(-180.0, 180.0),
            localTimeEpoch = System.currentTimeMillis() / 1000,
            localTime = LocationDateTime(
                LocalDateTime(
                    year = random.nextInt(2020, 2025),
                    monthNumber = random.nextInt(1, 12),
                    dayOfMonth = random.nextInt(1, 28),
                    hour = random.nextInt(0, 23),
                    minute = random.nextInt(0, 59),
                )
            ),
            timeZone = TimeZone.currentSystemDefault(),
            isSunUp = random.nextBoolean()
        )
    }

    private fun randomWeather(): Weather {

        return Weather(
            condition = conditions.random(random),
            temperature = random.nextDouble(-10.0, 35.0).toString(),
            humidity = random.nextInt(20, 90),
            feelsLikeTemperature = random.nextInt(-20, 60).toString(),
            isDay = random.nextBoolean(),
            conditionText = conditions.random(random).name,
            airQuality = randomAirQuality(),
            uvIndex = UvIndex(value = random.nextInt()),
            windSpeed = random.nextInt(0, 30).toString(),
            gustSpeed = random.nextInt(0, 30).toString(),
            windDirection = directions.random(random),
            windDegree = random.nextInt(0, 359),
            dewPoint = random.nextInt(0, 30),
            pressure = random.nextInt(700, 800),
            isRainingExpected = random.nextBoolean(),
            rainfallBeforeNow = persistentListOf(),
            rainfallAfterNow = persistentListOf(),
            rainfallNow = random.nextDouble(0.0, 10.0),
            maxRainfall = random.nextDouble(0.0, 10.0)
        )
    }

    private fun randomForecastWeather(): ForecastWeather {
        val hours = (0..23).map {
            ForecastHour(
                time = "$it:00",
                temperature = random.nextInt(-20, 30).toString(),
                condition = conditions.random(random),
                isDay = random.nextBoolean()
            )
        }
        val days = (1..5).map {
            ForecastDay(
                dayNumber = it.toString(),
                dayName = "$it - dayName",
                maxTemperature = random.nextInt(0, 30),
                minTemperature = random.nextInt(-20, -5),
                condition = conditions.random(random),
                totalRainFull = random.nextInt(0, 30)
            )
        }
        val today = ForecastToday(
            maxTemperature = random.nextInt(0, 30).toString(),
            minTemperature = random.nextInt(-20, -5).toString(),
            conditionCode = random.nextInt(0, 30),
            conditionText = conditions.random(random).name,
            totalUvIndex = random.nextInt(0, 12).toString(),
            rainChance = random.nextInt(0, 100).toString(),
            totalRainFull = random.nextInt(0, 30).toString(),
            sunrise = null,
            sunset = null

        )
        return ForecastWeather(
            today = today,
            hours = persistentListOf(*hours.toTypedArray()),
            days = persistentListOf(*days.toTypedArray())
        )
    }

    private fun randomAirQuality(): AirQuality {


        return AirQuality(
            aqiIndex = random.nextInt(),
            aqiText = "aqiText",
            coLevel = levels.random(random),
            no2Level = levels.random(random),
            o3Level = levels.random(random),
            so2Level = levels.random(random),
            pm25Level = levels.random(random),
            pm10Level = levels.random(random),
        )
    }

}