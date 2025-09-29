package com.contraomnese.weather.home.presentation

import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.FavoriteItem
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius1
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.itemWidth56
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import kotlinx.collections.immutable.toPersistentList

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
    onEvent: (HomeEvent) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
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
            if (uiState.inputLocation.value.isNotEmpty()) {
                MatchingLocations(uiState, onEvent, onNavigateToWeatherByLocation)
            } else FavoritesLocations(uiState, onEvent, onNavigateToWeatherByLocation)
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
    onEvent: (HomeEvent) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = {},
) {

    Column(
        modifier = Modifier
            .padding(horizontal = padding16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState.matchingLocations.forEach { location ->
            Row(
                modifier = Modifier
                    .clickable {
                        onNavigateToWeatherByLocation(
                            location.id,
                        )
                    }
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = buildAnnotatedString {
                        withStyle(
                            MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        ) {
                            append("${location.city}, ")
                        }
                        withStyle(
                            MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            )
                        ) {
                            append("${location?.state}, ${location?.country}")
                        }
                    },
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall.copy(
                        lineBreak = LineBreak.Paragraph
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.width(space8)
                )
                IconButton(
                    modifier = Modifier.width(itemWidth56),
                    onClick = {
                        if (uiState.favorites.any { it.id == location.id }) {
                            onEvent(HomeEvent.RemoveFavorite(location.id))
                        } else {
                            onEvent(HomeEvent.AddFavorite(location.id))
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (uiState.favorites.any { it.id == location.id }) WeatherIcons.RemoveFavorite else WeatherIcons.AddFavorite,
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
        OpenWebsiteLink(
            modifier = Modifier
                .padding(vertical = padding16)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            "https://locationiq.com",
            "Search by LocationIQ.com"
        )

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
            .padding(horizontal = padding16)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        items(uiState.favorites, key = { location -> location.id }) { location ->
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
                            location.id
                        )
                    },
                    onDeleteClicked = { onEvent(HomeEvent.RemoveFavorite(location.id)) }
                )
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(space16)
            )
        }
    }
}

@Composable
private fun OpenWebsiteLink(modifier: Modifier, url: String, description: String) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withLink(
            link = LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(
                    style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                }
            )
        ) {
            append(description)
        }
    }

    Text(
        modifier = modifier,
        text = annotatedText,
        textAlign = TextAlign.Center,
    )
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    val uiState = HomeUiState(
        matchingLocations = listOf(
            LocationInfoDomainModel.EMPTY.copy(
                city = "Москва",
                state = "Московская область",
                country = "Россия"
            ),
            LocationInfoDomainModel.EMPTY.copy(
                city = "Москоу",
                state = "Айдахо",
                country = "США"
            )
        ).toPersistentList()
    )

    WeatherTheme {
        HomeScreen(
            uiState
        )
    }
}