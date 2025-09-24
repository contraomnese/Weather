package com.contraomnese.weather.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.contraomnese.weather.MainActivityUiState
import com.contraomnese.weather.appsettings.navigation.appSettings
import com.contraomnese.weather.core.ui.widgets.NotificationSnackBar
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.itemHeight48
import com.contraomnese.weather.design.theme.itemWidth16
import com.contraomnese.weather.design.theme.itemWidth56
import com.contraomnese.weather.design.theme.padding20
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.home.navigation.home
import com.contraomnese.weather.home.navigation.navigateToHome
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationDestination
import com.contraomnese.weather.weatherByLocation.navigation.weatherByLocation

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun WeatherHost(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    uiState: MainActivityUiState,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val locationId = navBackStackEntry?.arguments?.getInt("locationId")

    val bottomBarVisible by remember(currentRoute) {
        mutableStateOf(
            when {
                currentRoute?.startsWith(WeatherByLocationDestination::class.java.name) == true -> true
                else -> false
            }
        )
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
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
        bottomBar = {
            if (bottomBarVisible) {
                BottomAppBar(
                    modifier = Modifier
                        .height(itemHeight48),
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = padding8),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(itemWidth56))
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(padding8)
                        ) {
                            uiState.favorites.forEach {
                                if (locationId != null && locationId == it.id) {
                                    Icon(
                                        modifier = Modifier.size(itemWidth16),
                                        imageVector = WeatherIcons.CircleFilled,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                } else {
                                    Icon(
                                        modifier = Modifier.size(itemWidth16),
                                        imageVector = WeatherIcons.Circle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            modifier = Modifier.width(itemWidth56),
                            onClick = { navController.navigateToHome() }
                        ) {
                            Icon(
                                imageVector = WeatherIcons.Menu,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = uiState.favorites.firstOrNull()?.let {
                    WeatherByLocationDestination(
                        locationId = it.id,
                        latitude = it.point.latitude.value,
                        longitude = it.point.longitude.value
                    )
                } ?: HomeDestination,
                enterTransition = { fadeIn() + scaleIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() + scaleIn() },
                popExitTransition = { fadeOut() }
            ) {
                home(externalNavigator = navController.homeNavigator())
                appSettings(externalNavigator = navController.appSettingsNavigator())
                weatherByLocation(externalNavigator = navController.weatherByLocationNavigator())
            }
        }
    }
}