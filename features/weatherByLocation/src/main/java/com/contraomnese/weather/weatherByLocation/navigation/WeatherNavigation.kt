package com.contraomnese.weather.weatherByLocation.navigation

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.contraomnese.weather.weatherByLocation.di.weatherModule
import com.contraomnese.weather.weatherByLocation.presentation.WeatherRoute
import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@Serializable
data class WeatherByLocationDestination(val locationId: Int, val latitude: Double, val longitude: Double)

fun NavController.navigateToWeatherByLocation(
    locationId: Int,
    latitude: Double,
    longitude: Double,
    navOptions: NavOptions? = null,
) {
    navigate(WeatherByLocationDestination(locationId = locationId, latitude = latitude, longitude = longitude), navOptions)
}

fun interface WeatherByLocationNavigator {
    fun onNavigateBack()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.weatherByLocation(
    externalNavigator: WeatherByLocationNavigator,
    pagerState: PagerState,
) {

    composable<WeatherByLocationDestination> { backStackEntry ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(weatherModule) }

        val weatherByLocationDestination = remember {
            backStackEntry.toRoute<WeatherByLocationDestination>()
        }

        val viewModel: WeatherViewModel = koinViewModel(
            viewModelStoreOwner = backStackEntry,
            parameters = {
                parametersOf(
                    weatherByLocationDestination.locationId,
                    weatherByLocationDestination.latitude,
                    weatherByLocationDestination.longitude
                )
            })

        WeatherRoute(
            viewModel = viewModel,
            onEvent = viewModel::onEvent,
            pagerState = pagerState
        )
    }
}