package com.contraomnese.weather.home.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.AlertGpsDialog
import com.contraomnese.weather.core.ui.widgets.AlertPermissionDialog
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
import com.contraomnese.weather.design.theme.itemWidth64
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.collections.immutable.toPersistentList

private const val ANIMATION_DURATION = 500

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToWeatherByLocation: (Int) -> Unit,
    onNavigateToAppSettings: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        when {
            uiState.isLoading ->
                LoadingIndicator(
                    Modifier
                        .align(Alignment.Center)
                        .width(itemWidth64)
                )

            else -> {
                HomeScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
                    onNavigateToAppSettings = onNavigateToAppSettings
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
    onNavigateToAppSettings: () -> Unit = {},
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var isFocused by remember { mutableStateOf(false) }
    var isSearchMode by remember { mutableStateOf(uiState.favoritesForecast.isNotEmpty()) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showGpsDialog by remember { mutableStateOf(false) }
    var requestGpsLocation by remember { mutableStateOf(false) }

    BackHandler(enabled = isSearchMode && uiState.favoritesForecast.isEmpty()) {
        isSearchMode = false
        focusManager.clearFocus(true)
    }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            isSearchMode = true
        }
    }

    LaunchedEffect(requestGpsLocation) {
        if (requestGpsLocation) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location = fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, null
                )
                location.addOnCompleteListener {
                    if (it.isSuccessful) {
                        onEvent(HomeEvent.GpsLocationChanged(lat = it.result.latitude, lon = it.result.longitude))
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(uiState.gpsLocation) {
        uiState.gpsLocation?.let {
            onNavigateToWeatherByLocation(it.id)
        }
    }

    val topBarPadding by animateDpAsState(
        targetValue = if (isSearchMode) 0.dp else screenHeight / 3,
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
        label = "topPadding"
    )

    Column {
        TopBar(
            modifier = Modifier
                .padding(top = topBarPadding)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            uiState = uiState,
            onEvent = onEvent,
            isSearchMode = isSearchMode,
            onNavigateToAppSettings = onNavigateToAppSettings,
            onPermissionRequest = {
                showPermissionDialog = true
            }
        )

        if (uiState.inputLocation.value.isNotEmpty()) {
            MatchingLocations(uiState, onEvent, onNavigateToWeatherByLocation)
        } else FavoritesLocations(uiState, onEvent, onNavigateToWeatherByLocation)

        if (showPermissionDialog) {
            AlertPermissionDialog(
                onDismissRequest = { showPermissionDialog = false },
                onPermissionGranted = { granted ->
                    showGpsDialog = granted
                },
                deniedTitle = R.string.permission_location_denied_title,
                firstTimeTitle = R.string.permission_location_first_time_title,
                permission = android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        if (showGpsDialog) {
            AlertGpsDialog(
                onDismissRequest = { showGpsDialog = false },
                onGpsEnabled = { enabled ->
                    requestGpsLocation = enabled
                }
            )
        }
    }
}


@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    isSearchMode: Boolean,
    onPermissionRequest: (Boolean) -> Unit = {},
    onNavigateToAppSettings: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier
                .padding(padding16)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SearchTextField(
                modifier = Modifier
                    .weight(1f),
                searchQuery = uiState.inputLocation.value,
                onSearchQueryChanged = { onEvent(HomeEvent.InputLocationChanged(it)) },
                isError = !uiState.inputLocation.isValidLocation(),
                placeholder = stringResource(R.string.search),
                leadingIcon = if (uiState.isSearching) {
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
            AnimatedVisibility(
                visible = isSearchMode,
                enter = expandHorizontally(animationSpec = tween(durationMillis = ANIMATION_DURATION)),
                exit = shrinkHorizontally(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            ) {
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
        AnimatedVisibility(
            visible = !isSearchMode,
            enter = fadeIn(animationSpec = tween(durationMillis = ANIMATION_DURATION)),
            exit = fadeOut(animationSpec = tween(durationMillis = ANIMATION_DURATION))
        ) {
            Button(
                onClick = { onPermissionRequest(true) },
                modifier = Modifier
                    .padding(horizontal = padding16)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            ) {
                Text(
                    text = stringResource(R.string.get_location_title),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W400),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
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
            .padding(horizontal = padding16)
            .wrapContentHeight(),
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
                            if (location.city.isNotEmpty()) {
                                append("${location.city}, ")
                            }

                        }
                        withStyle(
                            MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            )
                        ) {
                            if (location.state.isNotEmpty()) {
                                append("${location.state}, ")
                            }
                            if (location.country.isNotEmpty()) {
                                append(location.country)
                            }
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
            .wrapContentHeight(),
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