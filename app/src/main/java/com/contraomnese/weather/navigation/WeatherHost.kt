package com.contraomnese.weather.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.contraomnese.weather.core.ui.widgets.NotificationSnackBar
import com.contraomnese.weather.design.theme.padding40
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.home.navigation.home
import com.contraomnese.weather.locationforecast.navigation.locationForecast

@Composable
internal fun WeatherHost(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {

    Scaffold(
        snackbarHost = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = padding40, end = padding40, bottom = 400.dp)
            ) {
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1f),
                    hostState = snackBarHostState,
                    snackbar = { snackBarData ->
                        NotificationSnackBar(message = snackBarData.visuals.message)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeDestination
            ) {
                home(externalNavigator = navController.homeNavigator())
                locationForecast(externalNavigator = navController.locationForecastNavigator())
            }
        }
    }
}