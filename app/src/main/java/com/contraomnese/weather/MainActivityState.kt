package com.contraomnese.weather

import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.presentation.architecture.MviDestination
import com.contraomnese.weather.presentation.architecture.MviState
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationDestination

internal data class MainActivityState(
    override val isLoading: Boolean = true,
    val startDestination: MviDestination,
) : MviState {

    fun setWeatherDestinationId(id: Int?): MainActivityState = copy(
        isLoading = false,
        startDestination = id?.let { id ->
            WeatherByLocationDestination(id = id)
        } ?: HomeDestination
    )

    fun finishLoading(): MainActivityState = copy(isLoading = false)

    companion object {
        val DEFAULT = MainActivityState(startDestination = HomeDestination)
    }
}