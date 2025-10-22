package com.contraomnese.weather.weatherByLocation.presentation

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.MviEffect

internal sealed interface WeatherScreenEffect : MviEffect {
    data class InitialUpdated(val appSettings: AppSettings, val weather: Forecast, val favorites: List<Location>) : WeatherScreenEffect
    data class AppSettingsUpdated(val appSettings: AppSettings) : WeatherScreenEffect
    data class WeatherUpdated(val weather: Forecast) : WeatherScreenEffect
    data class FavoritesUpdated(val favorites: List<Location>) : WeatherScreenEffect
    data class LocationUpdated(val locationId: Int) : WeatherScreenEffect
}