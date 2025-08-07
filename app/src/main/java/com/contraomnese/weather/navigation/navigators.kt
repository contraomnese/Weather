package com.contraomnese.weather.navigation

import androidx.navigation.NavHostController
import com.contraomnese.weather.home.navigation.HomeNavigator
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationNavigator
import com.contraomnese.weather.weatherByLocation.navigation.navigateToWeatherByLocation

fun NavHostController.homeNavigator() = object : HomeNavigator {

    override fun onNavigateToWeatherByLocation(locationId: Int) {
        navigateToWeatherByLocation(locationId)
    }

    override fun onNavigateUp() {
        popBackStack()
    }
}

fun NavHostController.weatherByLocationNavigator() = WeatherByLocationNavigator { popBackStack() }