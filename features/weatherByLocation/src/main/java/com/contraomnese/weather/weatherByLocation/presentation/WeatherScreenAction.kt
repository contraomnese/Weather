package com.contraomnese.weather.weatherByLocation.presentation

import com.contraomnese.weather.presentation.architecture.MviAction

internal sealed interface WeatherScreenAction : MviAction {
    data object NavigateToHome : WeatherScreenAction
    data class SwapFavorite(val index: Int) : WeatherScreenAction
    data class AddFavorite(val locationId: Int) : WeatherScreenAction
}