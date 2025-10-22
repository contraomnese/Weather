package com.contraomnese.weather.weatherByLocation.presentation

import com.contraomnese.weather.presentation.architecture.MviEvent


internal sealed interface WeatherScreenEvent : MviEvent {
    data object NavigateToBack : WeatherScreenEvent
    data class SwapFavoriteGetWeather(val index: Int) : WeatherScreenEvent
    data class ShowError(val message: String?) : WeatherScreenEvent
}