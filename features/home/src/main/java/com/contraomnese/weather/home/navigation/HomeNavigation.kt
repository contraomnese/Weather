package com.contraomnese.weather.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.contraomnese.weather.home.di.homeModule
import com.contraomnese.weather.home.presentation.HomeRoute
import com.contraomnese.weather.home.presentation.HomeViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object HomeDestination

interface HomeNavigator {
    fun onNavigateToWeatherByLocation(locationId: Int, latitude: Double, longitude: Double)
    fun onNavigateToAppSettings()
    fun onNavigateUp()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.home(
    externalNavigator: HomeNavigator,
) {

    composable<HomeDestination> { backStackEntry ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(homeModule) }

        val viewModel: HomeViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)

        HomeRoute(
            viewModel = viewModel,
            onNavigateToWeatherByLocation = externalNavigator::onNavigateToWeatherByLocation,
            onNavigateToAppSettings = externalNavigator::onNavigateToAppSettings
        )
    }
}

fun NavHostController.navigateToHome() {
    navigate(HomeDestination)
}