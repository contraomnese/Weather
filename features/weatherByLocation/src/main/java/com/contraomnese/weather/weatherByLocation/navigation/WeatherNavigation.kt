package com.contraomnese.weather.weatherByLocation.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.contraomnese.weather.presentation.architecture.MviDestination
import com.contraomnese.weather.weatherByLocation.di.weatherModule
import com.contraomnese.weather.weatherByLocation.presentation.WeatherRoute
import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@Serializable
data class WeatherByLocationDestination(val id: Int) : MviDestination

fun NavController.navigateToWeatherByLocation(id: Int) {
    navigate(WeatherByLocationDestination(id = id)) {
        launchSingleTop = true
    }
}

interface WeatherByLocationNavigator {
    fun onNavigateBack()
    fun onNavigateToHome()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.weatherByLocation(
    externalNavigator: WeatherByLocationNavigator,
) {

    composable<WeatherByLocationDestination> { backStackEntry ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(weatherModule) }

        val locationId = remember {
            backStackEntry.toRoute<WeatherByLocationDestination>().id
        }

        val viewModel: WeatherViewModel = koinViewModel(
            viewModelStoreOwner = backStackEntry,
            parameters = { parametersOf(locationId) })

        WeatherRoute(
            viewModel = viewModel,
            eventFlow = viewModel.eventFlow,
            pushAction = viewModel::push,
            onNavigateToHome = externalNavigator::onNavigateToHome
        )
    }
}