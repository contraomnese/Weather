package com.contraomnese.weather.appsettings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.contraomnese.weather.appsettings.di.appSettingsModule
import com.contraomnese.weather.appsettings.presentation.AppSettingsRoute
import com.contraomnese.weather.appsettings.presentation.AppSettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object AppSettingsDestination

fun interface AppSettingsNavigator {
    fun onNavigateUp()
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.appSettings(
    externalNavigator: AppSettingsNavigator,
) {

    composable<AppSettingsDestination> { backStackEntry ->

        rememberKoinModules(unloadOnForgotten = true) { listOf(appSettingsModule) }

        val viewModel: AppSettingsViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)

        AppSettingsRoute(
            viewModel = viewModel
        )
    }
}

fun NavHostController.navigateToAppSettings() {
    navigate(AppSettingsDestination)
}