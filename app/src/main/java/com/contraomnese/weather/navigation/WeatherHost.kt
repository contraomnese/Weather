package com.contraomnese.weather.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.contraomnese.weather.MainActivityEvent
import com.contraomnese.weather.MainActivityState
import com.contraomnese.weather.appsettings.navigation.appSettings
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.home.navigation.home
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.presentation.utils.handleError
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationDestination
import com.contraomnese.weather.weatherByLocation.navigation.weatherByLocation
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun WeatherHost(
    navController: NavHostController = rememberNavController(),
    uiState: MainActivityState,
    eventFlow: Flow<MainActivityEvent>,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    eventFlow.collectEvent { event ->
        when (event) {
            is MainActivityEvent.HandleError -> snackbarHostState.showSnackbar(
                message = event.cause.handleError(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    val startDestination = remember {
        uiState.favorites.firstOrNull()?.let {
            WeatherByLocationDestination(id = it.id)
        } ?: HomeDestination
    }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        Box(modifier = Modifier
            .semantics {
                testTagsAsResourceId = true
            }
            .fillMaxSize()) {
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
}

