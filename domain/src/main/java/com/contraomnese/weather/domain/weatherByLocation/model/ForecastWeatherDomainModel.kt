package com.contraomnese.weather.domain.weatherByLocation.model

data class ForecastWeatherDomainModel(
    val locationInfo: LocationInfo,
    val currentInfo: CurrentInfo,
    val forecastInfo: ForecastInfo,
)

data class LocationInfo(
    val locationTimeEpoch: Long,
    val locationTime: String,
)

data class CurrentInfo(
    val temperatureC: String,
    val temperatureF: String,
    val feelsLikeC: String,
    val feelsLikeF: String,
    val isDay: Boolean,
    val conditionCode: Int,
    val conditionText: String,
    val windSpeedKph: String,
    val windSpeedMph: String,
    val windDirection: String,
    val windDegree: Int,
    val pressureMb: String,
    val pressureIn: String,
    val humidity: String,
    val uvIndex: String,
)

data class ForecastInfo(
    val today: ForecastToday,
    val forecastHours: List<ForecastHour>,
    val forecastDays: List<ForecastDay>,
)

data class ForecastToday(
    val maxTemperatureC: String,
    val maxTemperatureF: String,
    val minTemperatureC: String,
    val minTemperatureF: String,
    val conditionCode: Int,
    val conditionText: String,
    val totalUvIndex: String,
    val rainChance: String,
    val totalRainFullMm: String,
    val sunrise: String,
    val sunset: String,
)

data class ForecastHour(
    val time: String,
    val temperatureC: String,
    val temperatureF: String,
    val conditionCode: Int,
)

data class ForecastDay(
    val maxTemperatureC: String,
    val maxTemperatureF: String,
    val minTemperatureC: String,
    val minTemperatureF: String,
    val conditionCode: Int,
    val totalRainFullMm: Int,
)
