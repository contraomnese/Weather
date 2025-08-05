package com.contraomnese.weather.navigation

import androidx.navigation.NavHostController
import com.contraomnese.weather.home.navigation.HomeNavigator

fun NavHostController.homeNavigator() = object : HomeNavigator {

    override fun onNavigateToLocation(locationId: Int) {
        Unit
    }

    override fun onNavigateUp() {
        popBackStack()
    }
}