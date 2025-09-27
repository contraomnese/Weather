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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.contraomnese.weather.MainActivityEvent
import com.contraomnese.weather.MainActivityUiState
import com.contraomnese.weather.appsettings.navigation.appSettings
import com.contraomnese.weather.core.ui.widgets.NotificationSnackBar
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.itemHeight48
import com.contraomnese.weather.design.theme.itemWidth112
import com.contraomnese.weather.design.theme.itemWidth16
import com.contraomnese.weather.design.theme.itemWidth56
import com.contraomnese.weather.design.theme.padding20
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.home.navigation.home
import com.contraomnese.weather.home.navigation.navigateToHome
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationDestination
import com.contraomnese.weather.weatherByLocation.navigation.weatherByLocation
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun WeatherHost(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState,
    uiState: MainActivityUiState,
    onEvent: (MainActivityEvent) -> Unit,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val locationId = navBackStackEntry?.arguments?.getInt("locationId")
    val locationLat = navBackStackEntry?.arguments?.getDouble("latitude")
    val locationLon = navBackStackEntry?.arguments?.getDouble("longitude")

    val bottomBarVisible by remember(currentRoute) {
        mutableStateOf(currentRoute?.startsWith(WeatherByLocationDestination::class.java.name) ?: false)
    }

    val pagerState = rememberPagerState(
        initialPage = uiState.favorites.indexOfFirst { it.id == locationId }.coerceAtLeast(0),
        pageCount = { uiState.favorites.size }
    )

    val startDestination = remember {
        uiState.favorites.firstOrNull()?.let {
            WeatherByLocationDestination(
                locationId = it.id,
                latitude = it.point.latitude.value,
                longitude = it.point.longitude.value
            )
        } ?: HomeDestination
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
        snackbarHost = { WeatherSnackBarHost(snackBarHostState) },
        bottomBar = {
            if (bottomBarVisible) {
                WeatherBottomBar(uiState.favorites, locationId, pagerState, locationLat, locationLon, onEvent, navController)
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
                startDestination = startDestination,
                enterTransition = { fadeIn() + scaleIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() + scaleIn() },
                popExitTransition = { fadeOut() }
            ) {
                home(externalNavigator = navController.homeNavigator())
                appSettings(externalNavigator = navController.appSettingsNavigator())
                weatherByLocation(externalNavigator = navController.weatherByLocationNavigator(), pagerState = pagerState)
            }
        }
    }
}

@Composable
private fun WeatherBottomBar(
    favorites: ImmutableList<LocationInfoDomainModel>,
    locationId: Int?,
    pagerState: PagerState,
    locationLat: Double?,
    locationLon: Double?,
    onEvent: (MainActivityEvent) -> Unit,
    navController: NavHostController,
) {

    BottomAppBar(
        modifier = Modifier
            .height(itemHeight48)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(itemWidth56))
            if (favorites.firstOrNull { it.id == locationId } != null) {
                FavoritesIndicator(favorites, pagerState)
            } else {
                AddToFavoriteButton(locationId, locationLat, locationLon, onEvent)
            }
            HomeButton(navController)
        }
    }
}

@Composable
private fun HomeButton(navController: NavHostController) {
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

@Composable
private fun FavoritesIndicator(
    favorites: ImmutableList<LocationInfoDomainModel>,
    pagerState: PagerState,
) {
    val density = LocalDensity.current
    val lazyListState = rememberLazyListState()

    LaunchedEffect(pagerState.currentPage) {
        val centerOffset = (lazyListState.layoutInfo.viewportEndOffset -
                lazyListState.layoutInfo.viewportStartOffset) / 2

        lazyListState.animateScrollToItem(
            index = pagerState.currentPage,
            scrollOffset = (-centerOffset + with(density) { itemWidth16.toPx() / 2 }).toInt()
        )
    }

    LazyRow(
        modifier = Modifier.widthIn(max = itemWidth112),
        state = lazyListState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding8),
        userScrollEnabled = false
    ) {
        items(favorites.size) { index ->
            val isSelected = pagerState.currentPage == index
            Icon(
                modifier = Modifier.size(itemWidth16),
                imageVector = if (isSelected) WeatherIcons.CircleFilled else WeatherIcons.Circle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun AddToFavoriteButton(
    locationId: Int?,
    locationLat: Double?,
    locationLon: Double?,
    onEvent: (MainActivityEvent) -> Unit,
) {
    var clicked by remember { mutableStateOf(false) }

    IconButton(
        modifier = Modifier.width(itemWidth56),
        enabled = !clicked,
        onClick = {
            if (locationId != null && locationLat != null && locationLon != null) {
                onEvent(
                    MainActivityEvent.AddFavorite(
                        LocationInfoDomainModel.from(id = locationId, lat = locationLat, lon = locationLon)
                    )
                )
                clicked = true
            }
        }
    ) {
        Icon(
            imageVector = if (clicked) WeatherIcons.RemoveFavorite else WeatherIcons.AddFavorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun WeatherSnackBarHost(snackBarHostState: SnackbarHostState) {
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
}