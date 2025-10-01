package com.contraomnese.weather.home.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.AlertGpsDialog
import com.contraomnese.weather.core.ui.widgets.AlertPermissionDialog
import com.contraomnese.weather.core.ui.widgets.AutoSizeTitleText
import com.contraomnese.weather.core.ui.widgets.FavoriteItem
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius1
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemHeight64
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
import kotlinx.coroutines.delay

private const val ANIMATION_DURATION = 500

private data class GpsState(
    val isRequestToEnable: Boolean = false,
    val isPermissionGranted: Boolean = false,
    val isGpsEnabled: Boolean = false,
)

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

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("MissingPermission")
@Composable
internal fun BoxScope.HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
    onNavigateToAppSettings: () -> Unit = {},
) {
    val context = LocalContext.current
    val keyboardVisible = WindowInsets.isImeVisible
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var isSearchMode by remember { mutableStateOf(uiState.favoritesForecast.isNotEmpty()) }

    var gpsActivationState by remember {
        mutableStateOf(
            GpsState()
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gpsActivationState.isRequestToEnable) {
        if (gpsActivationState.isRequestToEnable) {
            delay(1000)
            showDialog = true
        } else {
            showDialog = false
        }
    }

    LaunchedEffect(uiState.favoritesForecast) {
        if (uiState.favoritesForecast.isEmpty()) isSearchMode = false
    }

    LaunchedEffect(keyboardVisible) {
        if (uiState.favoritesForecast.isEmpty()) {
            isSearchMode = keyboardVisible
        }
    }

    LaunchedEffect(gpsActivationState) {
        if (gpsActivationState.isGpsEnabled) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location = fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, null
                )
                location.addOnSuccessListener {
                    if (it != null) {
                        onEvent(HomeEvent.GpsLocationChanged(lat = it.latitude, lon = it.longitude))
                    } else {
                        Toast.makeText(context, "Can't find location", Toast.LENGTH_SHORT).show()
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
        targetValue = if (isSearchMode) 0.dp else with(density) { (screenHeight.toPx() / 2.5f).toDp() },
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    )

    AutoSizeTitleText(
        modifier = Modifier.padding(top = screenHeight / 4),
        visible = !isSearchMode,
        enter = expandVertically(animationSpec = tween(durationMillis = ANIMATION_DURATION)),
        exit = shrinkVertically(animationSpec = tween(durationMillis = ANIMATION_DURATION)),
        title = stringResource(R.string.use_search_title),
        maxLines = 2
    )

    Column {
        TopBar(
            modifier = Modifier
                .padding(top = topBarPadding),
            uiState = uiState,
            onEvent = onEvent,
            isSearchMode = isSearchMode,
            onNavigateToAppSettings = onNavigateToAppSettings,
            onPermissionRequest = {
                gpsActivationState = gpsActivationState.copy(isRequestToEnable = true)
            }
        )

        if (uiState.inputLocation.value.isNotEmpty()) {
            MatchingLocations(uiState, onEvent, onNavigateToWeatherByLocation)
        } else FavoritesLocations(uiState, onEvent, onNavigateToWeatherByLocation)

        if (showDialog) {
            AlertPermissionDialog(
                onDismissRequest = { gpsActivationState = gpsActivationState.copy(isRequestToEnable = false) },
                onPermissionGranted = { granted ->
                    gpsActivationState = gpsActivationState.copy(isPermissionGranted = granted)
                },
                deniedTitle = R.string.permission_location_denied_title,
                firstTimeTitle = R.string.permission_location_first_time_title,
                permission = android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        if (gpsActivationState.isPermissionGranted && showDialog) {
            AlertGpsDialog(
                onDismissRequest = { gpsActivationState = gpsActivationState.copy(isRequestToEnable = false) },
                onGpsEnabled = { enabled ->
                    gpsActivationState = gpsActivationState.copy(isGpsEnabled = enabled)
                }
            )
        }
    }
    AutoSizeTitleText(
        modifier = Modifier.padding(bottom = screenHeight / 4),
        alignment = Alignment.BottomEnd,
        visible = !isSearchMode,
        enter = expandVertically(
            animationSpec = tween(durationMillis = ANIMATION_DURATION),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION
            ),
            shrinkTowards = Alignment.Top
        ),
        title = stringResource(R.string.use_gps_title),
        textAlign = TextAlign.End,
        maxLines = 2
    )
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
            val interactionSource = remember { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(if (pressed) 0.85f else 1f)

            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier
                        .size(itemHeight64)
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(
                                bounded = true,
                                radius = itemHeight64,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 1f)
                            )
                        ) { }
                        .align(Alignment.Center),
                    onClick = { onPermissionRequest(true) },
                    interactionSource = interactionSource,
                ) {
                    Icon(
                        imageVector = WeatherIcons.GPS,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
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

@Preview(showBackground = false, showSystemUi = false, device = "id:pixel_5")
@Composable
private fun HomeScreenPreview() {
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
        ).toPersistentList(),
        favorites = listOf(
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
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            HomeScreen(
                uiState
            )
        }

    }
}