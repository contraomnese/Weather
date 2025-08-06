package com.contraomnese.weather.locationforecast.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.contraomnese.weather.locationforecast.di.locationForecastModule
import com.contraomnese.weather.locationforecast.presentation.LocationForecastRoute
import com.contraomnese.weather.locationforecast.presentation.LocationForecastViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@Serializable
private data class LocationForecastDestination(val locationId: Int)

fun NavController.navigateToLocationForecast(
    locationId: Int,
    navOptions: NavOptions? = null,
) {
    navigate(LocationForecastDestination(locationId), navOptions)
}

fun interface LocationForecastNavigator {
    fun onNavigateBack()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.locationForecast(
    externalNavigator: LocationForecastNavigator,
) {

    composable<LocationForecastDestination> { backStackEntry ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(locationForecastModule) }

        val locationForecastDestination = remember {
            backStackEntry.toRoute<LocationForecastDestination>()
        }

        val viewModel: LocationForecastViewModel = koinViewModel(
            viewModelStoreOwner = backStackEntry,
            parameters = {
                parametersOf(locationForecastDestination.locationId)
            })

        LocationForecastRoute(
            viewModel = viewModel
        )
    }
}