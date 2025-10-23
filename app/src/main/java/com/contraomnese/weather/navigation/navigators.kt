package com.contraomnese.weather.navigation

import androidx.navigation.NavHostController
import com.contraomnese.weather.appsettings.navigation.AppSettingsNavigator
import com.contraomnese.weather.appsettings.navigation.navigateToAppSettings
import com.contraomnese.weather.home.navigation.HomeNavigator
import com.contraomnese.weather.home.navigation.navigateToHome
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationNavigator
import com.contraomnese.weather.weatherByLocation.navigation.navigateToWeatherByLocation

fun NavHostController.homeNavigator() = object : HomeNavigator {

    override fun onNavigateToWeatherByLocation(id: Int) {
        popBackStack()
        navigateToWeatherByLocation(id = id)
    }

    override fun onNavigateToAppSettings() {
        navigateToAppSettings()
    }

    override fun onNavigateUp() {
        popBackStack()
    }
}

fun NavHostController.weatherByLocationNavigator() = object : WeatherByLocationNavigator {
    override fun onNavigateBack() {
        popBackStack()
    }

    override fun onNavigateToHome() {
        navigateToHome()
    }
}

fun NavHostController.appSettingsNavigator() = AppSettingsNavigator { popBackStack() }