package com.contraomnese.weather.navigation

import androidx.navigation.NavHostController
import com.contraomnese.weather.home.navigation.HomeNavigator
import com.contraomnese.weather.locationforecast.navigation.LocationForecastNavigator
import com.contraomnese.weather.locationforecast.navigation.navigateToLocationForecast

fun NavHostController.homeNavigator() = object : HomeNavigator {

    override fun onNavigateToLocationForecastBy(locationId: Int) {
        navigateToLocationForecast(locationId)
    }

    override fun onNavigateUp() {
        popBackStack()
    }
}

fun NavHostController.locationForecastNavigator() = LocationForecastNavigator { popBackStack() }