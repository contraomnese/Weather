package com.contraomnese.weather.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.FavoriteItem
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius1
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemHeight40
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding32
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.random.Random

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToWeatherByLocation: (Int) -> Unit,
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
    onNavigateToWeatherByLocation: (Int) -> Unit = {},
    onNavigateToAppSettings: () -> Unit = {},
) {

    Column(
        modifier = Modifier.padding(top = padding32)
    ) {
        TopBar(uiState, onEvent, onNavigateToAppSettings)
        MatchingLocations(uiState, onEvent, onNavigateToWeatherByLocation)
        if (uiState.inputLocation.value.isEmpty()) {
            FavoritesLocations(uiState, onEvent, onNavigateToWeatherByLocation)
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
            .padding(padding16)
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
    onNavigateToWeatherByLocation: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = padding16)
    ) {
        uiState.matchingLocations.forEach { location ->
            Row(
                modifier = Modifier
                    .clickable { onNavigateToWeatherByLocation(location.id) }
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
                Text(
                    modifier = Modifier.padding(horizontal = padding16),
                    text = location.countryName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (location.isFavorite || uiState.favorites.any { it.id == location.id }) {
                            onEvent(HomeEvent.RemoveFavorite(location.id))
                        } else {
                            onEvent(HomeEvent.AddFavorite(location.id))
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (location.isFavorite || uiState.favorites.any { it.id == location.id }) WeatherIcons.RemoveFavorite else WeatherIcons.AddFavorite,
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
    onNavigateToWeatherByLocation: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(padding16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        items(uiState.favorites) { location ->
            val favoriteForecast = uiState.favoritesForecast[location.id]

            if (favoriteForecast != null) {
                FavoriteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight140),
                    locationName = location.name,
                    locationTime = favoriteForecast.locationInfo.locationTime?.toLocalTime() ?: "00:00",
                    temperature = favoriteForecast.currentInfo.temperature,
                    maxTemperature = favoriteForecast.forecastInfo.today.maxTemperature,
                    minTemperature = favoriteForecast.forecastInfo.today.minTemperature,
                    condition = favoriteForecast.currentInfo.conditionText,
                    onTapClicked = { onNavigateToWeatherByLocation(location.id) },
                    onDeleteClicked = { onEvent(HomeEvent.RemoveFavorite(location.id)) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )
        val original = MatchingLocationDomainModel(
            id = 1,
            name = "",
            countryName = "",
            isFavorite = Random.nextBoolean()
        )
        val cities = List(10) { index ->
            original.copy(
                id = index,
                name = "City $index",
                countryName = "Country $index"
            )
        }

        HomeScreen(
            uiState = HomeUiState(matchingLocations = cities.toPersistentList()),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenLoadingPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )

        HomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                favorites = persistentListOf(
                    DetailsLocationDomainModel(
                        id = 1, name = "New York",
                        point = CoordinatesDomainModel(
                            latitude = LatitudeDomainModel(
                                value = 4.5
                            ), longitude = LongitudeDomainModel(value = 6.7)
                        ),
                    ),
                    DetailsLocationDomainModel(
                        id = 2, name = "Moscow",
                        point = CoordinatesDomainModel(
                            latitude = LatitudeDomainModel(
                                value = 4.5
                            ), longitude = LongitudeDomainModel(value = 6.7)
                        ),
                    ),
                )
            ),
            onEvent = {}, onNavigateToWeatherByLocation = {}, onNavigateToAppSettings = {}
        )
    }
}