package com.contraomnese.weather.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.contraomnese.weather.MainActivityState
import com.contraomnese.weather.appsettings.navigation.appSettings
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.home.navigation.home
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationDestination
import com.contraomnese.weather.weatherByLocation.navigation.weatherByLocation

@Composable
internal fun WeatherHost(
    navController: NavHostController = rememberNavController(),
    uiState: MainActivityState,
) {

    val startDestination = remember {
        uiState.favorites.firstOrNull()?.let {
            WeatherByLocationDestination(id = it.id)
        } ?: HomeDestination
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() + scaleIn() },
            popExitTransition = { fadeOut() }
        ) {
            home(externalNavigator = navController.homeNavigator())
            appSettings(externalNavigator = navController.appSettingsNavigator())
            weatherByLocation(
                externalNavigator = navController.weatherByLocationNavigator()
            )
        }
    }

}

