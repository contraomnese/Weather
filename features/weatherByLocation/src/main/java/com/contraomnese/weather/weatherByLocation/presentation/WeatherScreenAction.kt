package com.contraomnese.weather.weatherByLocation.presentation

import com.contraomnese.weather.presentation.architecture.MviAction

internal sealed interface WeatherScreenAction : MviAction {
    data object ClickButtonBack : WeatherScreenAction
    data class SwapFavorite(val index: Int) : WeatherScreenAction
}