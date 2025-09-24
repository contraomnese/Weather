package com.contraomnese.weather.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.FavoriteItem
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.cornerRadius1
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemHeight40
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space32

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToWeatherByLocation: (Int, Double, Double) -> Unit,
    onNavigateToAppSettings: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
        onNavigateToAppSettings = onNavigateToAppSettings
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToWeatherByLocation: (Int, Double, Double) -> Unit = { _, _, _ -> },
    onNavigateToAppSettings: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column {
            TopBar(uiState, onEvent, onNavigateToAppSettings)
            MatchingLocations(uiState, onEvent, onNavigateToWeatherByLocation)
            if (uiState.inputLocation.value.isEmpty()) {
                FavoritesLocations(uiState, onEvent, onNavigateToWeatherByLocation)
            }
        }
    }
}

@Composable
private fun TopBar(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToAppSettings: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = padding16, end = padding16, bottom = padding16)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SearchTextField(
            modifier = Modifier
                .weight(1f),
            searchQuery = uiState.inputLocation.value,
            onSearchQueryChanged = { onEvent(HomeEvent.LocationChanged(it)) },
            isError = !uiState.inputLocation.isValidLocation(),
            placeholder = stringResource(R.string.search_city),
            leadingIcon = if (uiState.isLoading) {
                {
                    LoadingIndicator(
                        modifier =
                            Modifier
                                .height(itemHeight20)
                                .aspectRatio(1f)
                    )
                }
            } else null,
            trailingIcon = if (uiState.inputLocation.value.isEmpty()) {
                {
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(
                            imageVector = WeatherIcons.Map,
                            contentDescription = null
                        )
                    }
                }
            } else null
        )

        Icon(
            modifier = Modifier
                .padding(start = padding8)
                .clickable { onNavigateToAppSettings() },
            imageVector = WeatherIcons.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun MatchingLocations(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToWeatherByLocation: (Int, Double, Double) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = padding16)
    ) {
        uiState.matchingLocations.forEach { location ->
            Row(
                modifier = Modifier
                    .clickable { onNavigateToWeatherByLocation(location.id, location.point.latitude.value, location.point.longitude.value) }
                    .fillMaxWidth()
                    .height(itemHeight40),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                location.countryName?.let {
                    Text(
                        modifier = Modifier.padding(horizontal = padding16),
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (uiState.favorites.any { it == location }) {
                            onEvent(HomeEvent.RemoveFavorite(location.id))
                        } else {
                            onEvent(HomeEvent.AddFavorite(location))
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (uiState.favorites.contains(location)) WeatherIcons.RemoveFavorite else WeatherIcons.AddFavorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(itemThickness2)
                    .background(
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                        RoundedCornerShape(cornerRadius1)
                    )
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FavoritesLocations(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToWeatherByLocation: (Int, Double, Double) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = padding16)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        items(uiState.favorites) { location ->
            val favoriteForecast = uiState.favoritesForecast[location.id]
            favoriteForecast?.let {
                FavoriteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight160),
                    locationName = it.locationInfo.name,
                    locationCountry = it.locationInfo.country,
                    timeZone = it.locationInfo.timeZone,
                    temperature = it.currentInfo.temperature,
                    maxTemperature = it.forecastInfo.today.maxTemperature,
                    minTemperature = it.forecastInfo.today.minTemperature,
                    conditionText = it.currentInfo.conditionText,
                    condition = it.currentInfo.condition,
                    onTapClicked = {
                        onNavigateToWeatherByLocation(
                            location.id,
                            location.point.latitude.value,
                            location.point.longitude.value
                        )
                    },
                    onDeleteClicked = { onEvent(HomeEvent.RemoveFavorite(location.id)) }
                )
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(space32)
            )
        }
    }
}