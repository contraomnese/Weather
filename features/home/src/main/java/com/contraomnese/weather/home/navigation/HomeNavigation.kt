package com.contraomnese.weather.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.contraomnese.weather.home.di.homeModule
import com.contraomnese.weather.home.presentation.HomeRoute
import com.contraomnese.weather.home.presentation.HomeViewModel
import com.contraomnese.weather.presentation.architecture.MviDestination
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object HomeDestination : MviDestination

interface HomeNavigator {
    fun onNavigateToWeatherByLocation(id: Int)
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
    popBackStack()
    navigate(HomeDestination)
}