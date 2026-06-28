package com.contraomnese.weather.data.mappers.forecast

import com.contraomnese.weather.data.mappers.forecast.weatherapi.toMaxTemperatureDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toMinTemperatureDomain
import com.contraomnese.weather.data.mappers.forecast.weatherapi.toTemperatureDomain
import com.contraomnese.weather.data.storage.db.locations.dto.FavoriteForecastData
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlinx.datetime.TimeZone

fun FavoriteForecastData.toDomain(appSettings: AppSettings): FavoriteForecast {

    val timeZone = TimeZone.of(location.timeZoneId)

    return FavoriteForecast(
        locationId = location.locationId,
        locationName = location.name,
        locationCountry = location.country,
        timeZone = timeZone,
        temperature = todayForecast.toTemperatureDomain(appSettings.temperatureUnit),
        maxTemperature = todayForecast.toMaxTemperatureDomain(appSettings.temperatureUnit),
        minTemperature = todayForecast.toMinTemperatureDomain(appSettings.temperatureUnit),
        condition = WeatherCondition.fromWeatherApi(todayForecast.conditionCode),
    )
}




