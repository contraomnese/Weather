package com.contraomnese.weather.home.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Trace
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.core.ui.composition.LocalWeatherBackgrounds
import com.contraomnese.weather.core.ui.composition.weatherBackgrounds
import com.contraomnese.weather.core.ui.widgets.AnimatedCircleButton
import com.contraomnese.weather.core.ui.widgets.AnimatedIcon
import com.contraomnese.weather.core.ui.widgets.FavoriteItem
import com.contraomnese.weather.core.ui.widgets.GpsModeAlertDialog
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.PermissionAlertDialog
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.core.ui.widgets.WeatherSnackBarHost
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
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.presentation.utils.handleError
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Instant

private const val ANIMATION_DURATION = 500

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToWeatherByLocation: (Int) -> Unit,
    onNavigateToAppSettings: () -> Unit,
) {

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val snackBarHostState = LocalSnackbarHostState.current

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3989BF),
                Color(0xFF317FB3),
                Color(0xFF2C72A1),
                Color(0xFF266289),
            )
        )
    }
    Scaffold(
        snackbarHost = { WeatherSnackBarHost(snackBarHostState) },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading ->
                    LoadingIndicator(
                        Modifier
                            .align(Alignment.Center)
                            .width(itemWidth64)
                    )

                else -> HomeScreen(
                    uiState = uiState,
                    snackBar = snackBarHostState,
                    eventFlow = viewModel.eventFlow,
                    pushAction = viewModel::push,
                    onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
                    onNavigateToAppSettings = onNavigateToAppSettings
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenState,
    eventFlow: Flow<HomeScreenEvent>,
    snackBar: SnackbarHostState,
    pushAction: (HomeScreenAction) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
    onNavigateToAppSettings: () -> Unit = {},
) {
    val context = LocalContext.current

    eventFlow.collectEvent { event ->
        when (event) {

            is HomeScreenEvent.GetGpsLocation -> {
                getGpsLocation(context, pushAction)
            }

            is HomeScreenEvent.HandleError -> snackBar.showSnackbar(
                message = event.cause.handleError(context),
                duration = SnackbarDuration.Short
            )

            is HomeScreenEvent.NavigateToGpsLocation -> onNavigateToWeatherByLocation(event.id)
        }
    }

    when {
        uiState.favorites.isEmpty() -> WelcomeScreen(
            uiState = uiState,
            pushAction = pushAction,
            onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
            onNavigateToAppSettings = onNavigateToAppSettings
        )

        else -> MainScreen(
            uiState = uiState,
            pushAction = pushAction,
            onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
            onNavigateToAppSettings = onNavigateToAppSettings
        )
    }
}

@SuppressLint("MissingPermission")
@Composable
internal fun MainScreen(
    uiState: HomeScreenState,
    pushAction: (HomeScreenAction) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
    onNavigateToAppSettings: () -> Unit = {},
) {

    Column {
        SearchBar(
            modifier = Modifier.testTag("search_bar"),
            inputLocation = uiState.inputLocation,
            isSearchMode = true,
            isSearching = uiState.isSearching,
            pushAction = pushAction,
            onNavigateToAppSettings = onNavigateToAppSettings,
            onGpsClickButton = {
                pushAction(HomeScreenAction.SwitchGpsMode(true))
            }
        )
        Trace.endSection()

        if (uiState.inputLocation.text.isNotEmpty()) {
            MatchingLocations(
                uiState.matchingLocations,
                uiState.favorites,
                pushAction,
                onNavigateToWeatherByLocation
            )
        } else FavoritesLocations(
            uiState.currentTime,
            uiState.favorites,
            uiState.favoritesForecast,
            pushAction,
            onNavigateToWeatherByLocation
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("MissingPermission")
@Composable
internal fun WelcomeScreen(
    uiState: HomeScreenState,
    pushAction: (HomeScreenAction) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
    onNavigateToAppSettings: () -> Unit = {},
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val searchBarOnTop = WindowInsets.isImeVisible

    val topBarOffset by animateDpAsState(
        targetValue = if (searchBarOnTop) 0.dp else screenHeight / 2.5f,
        animationSpec = tween(ANIMATION_DURATION)
    )

    Column(
        modifier = Modifier
            .offset {
                val yPx = with(this) { topBarOffset.roundToPx() }
                IntOffset(x = 0, y = yPx)
            }
    ) {
        SearchBar(
            modifier = Modifier.testTag("search_bar"),
            inputLocation = uiState.inputLocation,
            isSearchMode = searchBarOnTop,
            isSearching = uiState.isSearching,
            pushAction = pushAction,
            onNavigateToAppSettings = onNavigateToAppSettings,
            onGpsClickButton = {
                pushAction(HomeScreenAction.SwitchGpsMode(true))
            }
        )

        if (uiState.inputLocation.text.isNotEmpty() && searchBarOnTop) {
            MatchingLocations(uiState.matchingLocations, uiState.favorites, pushAction, onNavigateToWeatherByLocation)
        } else FavoritesLocations(
            uiState.currentTime,
            uiState.favorites,
            uiState.favoritesForecast,
            pushAction,
            onNavigateToWeatherByLocation
        )

        if (uiState.gps.isGpsMode) {
            PermissionAlertDialog(
                onDismissRequest = { pushAction(HomeScreenAction.SwitchGpsMode(false)) },
                onPermissionGranted = { granted ->
                    pushAction(HomeScreenAction.AccessFineLocationPermissionGranted(granted))
                },
                deniedTitle = R.string.permission_location_denied_title,
                firstTimeTitle = R.string.permission_location_first_time_title,
                permission = android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        if (uiState.gps.isPermissionGranted && uiState.gps.isGpsMode) {
            GpsModeAlertDialog(
                onDismissRequest = { pushAction(HomeScreenAction.DeviceGpsModeEnabled(false)) },
                onGpsModeEnabled = { enabled ->
                    pushAction(HomeScreenAction.DeviceGpsModeEnabled(enabled))
                }
            )
        }
    }
}


@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    inputLocation: TextFieldValue,
    isSearchMode: Boolean,
    isSearching: Boolean,
    pushAction: (HomeScreenAction) -> Unit = {},
    onGpsClickButton: (Boolean) -> Unit = {},
    onNavigateToAppSettings: () -> Unit,
) {
    Column {
        TextSearchBar(modifier, inputLocation, pushAction, isSearching, isSearchMode, onNavigateToAppSettings)
        GpsSearchButton(modifier, isSearchMode, onGpsClickButton)
    }

}

@Composable
private fun GpsSearchButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onClick: (Boolean) -> Unit,
) {
    AnimatedCircleButton(modifier, isVisible, onClick, ANIMATION_DURATION)
}

@Composable
private fun TextSearchBar(
    modifier: Modifier,
    inputLocation: TextFieldValue,
    pushAction: (HomeScreenAction) -> Unit,
    isSearching: Boolean,
    isSearchMode: Boolean,
    onNavigateToAppSettings: () -> Unit,
) {
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
            searchQuery = inputLocation,
            onSearchQueryChanged = { pushAction(HomeScreenAction.InputLocation(it)) },
            isError = !inputLocation.hasValidLocation(),
            placeholder = stringResource(R.string.search),
            leadingIcon = if (isSearching) {
                {
                    LoadingIndicator(
                        modifier = Modifier
                            .height(itemHeight20)
                            .aspectRatio(1f)
                    )
                }
            } else null,
            trailingIcon = if (inputLocation.text.isEmpty()) {
                {
                    IconButton(onClick = { }) {
                        Icon(imageVector = WeatherIcons.Map, contentDescription = null)
                    }
                }
            } else null
        )
        AnimatedIcon(isSearchMode, onNavigateToAppSettings, ANIMATION_DURATION)
    }
}

@Composable
private fun MatchingLocations(
    locations: ImmutableList<Location>,
    favorites: ImmutableList<Location>,
    pushAction: (HomeScreenAction) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit = {},
) {

    val locationsInFavorites = remember(locations, favorites) {
        val favoriteIds = favorites.map { it.id }.toSet()
        locations.associateWith { it.id in favoriteIds }
    }

    LazyColumn(
        modifier = Modifier
            .testTag("matching_locations")
            .padding(horizontal = padding16)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = locations,
            key = { it.id }
        ) { location ->
            MatchLocation(
                onClick = { onNavigateToWeatherByLocation.invoke(location.id) },
                onFavoriteClick = {
                    if (locationsInFavorites.getOrDefault(location, false)) {
                        pushAction(HomeScreenAction.RemoveFavorite(location.id))
                    } else {
                        pushAction(HomeScreenAction.AddFavorite(location.id))
                    }
                },
                favoriteIcon =
                    if (locationsInFavorites.getOrDefault(location, false)) WeatherIcons.RemoveFavorite
                    else WeatherIcons.AddFavorite,
                city = location.city,
                country = location.country,
                state = location.state
            )
        }
        item {
            LocationApiLink(
                modifier = Modifier
                    .padding(vertical = padding16)
                    .fillMaxWidth(),
                "https://locationiq.com",
                "Search by LocationIQ.com"
            )
        }
    }
}

@Composable
private fun MatchLocation(
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    favoriteIcon: ImageVector,
    city: String?,
    country: String?,
    state: String?,
) {

    val cityStyle = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
        color = MaterialTheme.colorScheme.onSurface,
    )

    val stateStyle = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    )

    val matchLocationText = remember(city, country, state) {
        buildAnnotatedString {
            withStyle(
                cityStyle
            ) {
                city?.let {
                    append("${it}, ")
                } ?: ""
            }
            withStyle(
                stateStyle
            ) {
                state?.let {
                    append("${it}, ")
                } ?: ""
                country?.let {
                    append(it)
                } ?: ""
            }
        }
    }

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            modifier = Modifier.weight(1f, fill = false),
            text = matchLocationText,
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
            onClick = onFavoriteClick
        ) {
            Icon(
                imageVector = favoriteIcon,
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

@Composable
private fun FavoritesLocations(
    currentTime: Instant,
    favorites: ImmutableList<Location>,
    favoritesForecast: ImmutableMap<Int, Forecast>,
    pushAction: (HomeScreenAction) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit,
) {
    val listState = rememberSaveable(
        saver = LazyListState.Saver
    ) {
        LazyListState()
    }

    val backgrounds = LocalWeatherBackgrounds.current

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = padding16)
            .fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        items(favorites, key = { location -> location.id }) { location ->
            favoritesForecast[location.id]?.let {
                FavoriteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight160),
                    locationName = it.location.city,
                    locationCountry = it.location.country,
                    timeZone = it.location.timeZone,
                    temperature = it.today.temperature,
                    maxTemperature = it.forecast.today.maxTemperature,
                    minTemperature = it.forecast.today.minTemperature,
                    conditionText = it.today.conditionText,
                    background = backgrounds.getValue(it.today.condition).resId,
                    onTapClicked = {
                        onNavigateToWeatherByLocation(
                            location.id
                        )
                    },
                    currentTime = currentTime,
                    onDeleteClicked = { pushAction(HomeScreenAction.RemoveFavorite(location.id)) }
                )
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(space8)
            )
        }
    }
}

@Composable
private fun LocationApiLink(modifier: Modifier, url: String, description: String) {
    val context = LocalContext.current

    val style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
        color = MaterialTheme.colorScheme.onBackground,
        textDecoration = TextDecoration.Underline
    )

    val annotatedText = remember(url, description) {
        buildAnnotatedString {
            withLink(
                link = LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(style = style),
                    linkInteractionListener = {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                )
            ) {
                append(description)
            }
        }
    }

    Text(
        modifier = modifier,
        text = annotatedText,
        textAlign = TextAlign.Center,
    )
}

@SuppressLint("MissingPermission")
private fun getGpsLocation(
    context: Context,
    pushAction: (HomeScreenAction) -> Unit,
) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        val location = fusedClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        )
        location.addOnSuccessListener {
            if (it != null) {
                pushAction(HomeScreenAction.UpdateGpsLocation(lat = it.latitude, lon = it.longitude))
            } else {
                Toast.makeText(context, "Can't find location", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = false, showSystemUi = false, device = "id:pixel_5")
@Composable
private fun HomeScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = HomeScreenState(
        matchingLocations = emptyList<Location>().toPersistentList(),
        favorites = emptyList<Location>().toPersistentList()
    )

    WeatherTheme {
        CompositionLocalProvider(LocalWeatherBackgrounds provides weatherBackgrounds) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                HomeScreen(
                    uiState,
                    eventFlow = emptyFlow(),
                    snackbarHostState
                )
            }
        }
    }
}