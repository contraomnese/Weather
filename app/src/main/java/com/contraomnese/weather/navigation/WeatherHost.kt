package com.contraomnese.weather.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.contraomnese.weather.appsettings.navigation.appSettings
import com.contraomnese.weather.core.ui.widgets.NotificationSnackBar
import com.contraomnese.weather.design.theme.padding20
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.home.navigation.home
import com.contraomnese.weather.weatherByLocation.navigation.weatherByLocation

@Composable
internal fun WeatherHost(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {

    Scaffold(
        snackbarHost = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = padding20)
            ) {
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = padding8)
                        .zIndex(1f),
                    hostState = snackBarHostState,
                    snackbar = { snackBarData ->
                        NotificationSnackBar(message = snackBarData.visuals.message)
                    }
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeDestination
            ) {
                home(externalNavigator = navController.homeNavigator())
                appSettings(externalNavigator = navController.appSettingsNavigator())
                weatherByLocation(externalNavigator = navController.weatherByLocationNavigator())
            }
        }
    }
}