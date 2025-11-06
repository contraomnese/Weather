package com.contraomnese.weather.home.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.core.ui.widgets.AnimatedAutoSizeTitleText
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
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.presentation.utils.handleError
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

private const val ANIMATION_DURATION = 500

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToWeatherByLocation: (Int) -> Unit,
    onNavigateToAppSettings: () -> Unit,
) {

    val snackBarHostState = LocalSnackbarHostState.current
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { WeatherSnackBarHost(snackBarHostState) },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        eventFlow = viewModel.eventFlow,
                        pushAction = viewModel::push,
                        snackBarHostState = snackBarHostState,
                        onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
                        onNavigateToAppSettings = onNavigateToAppSettings
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("MissingPermission")
@Composable
internal fun BoxScope.HomeScreen(
    uiState: HomeScreenState,
    eventFlow: Flow<HomeScreenEvent>,
    pushAction: (HomeScreenAction) -> Unit = {},
    snackBarHostState: SnackbarHostState,
    onNavigateToWeatherByLocation: (Int) -> Unit = { },
    onNavigateToAppSettings: () -> Unit = {},
) {
    val context = LocalContext.current
    val keyboardVisible = WindowInsets.isImeVisible
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var searchBarOnTop by remember { mutableStateOf(uiState.favorites.isNotEmpty()) }
    var gpsModeEnabled by remember { mutableStateOf(false) }

    eventFlow.collectEvent { event ->
        when (event) {
            is HomeScreenEvent.SwitchGpsMode -> {
                gpsModeEnabled = event.enabled
            }

            is HomeScreenEvent.GetGpsLocation -> {
                getGpsLocation(context, pushAction)
            }

            is HomeScreenEvent.OnSearchBarTop -> {
                searchBarOnTop = event.onTop
            }

            is HomeScreenEvent.HandleError -> snackBarHostState.showSnackbar(
                message = event.cause.handleError(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(keyboardVisible) {
        if (uiState.favorites.isEmpty()) {
            searchBarOnTop = keyboardVisible
        }
    }

    LaunchedEffect(uiState.gps.location) {
        uiState.gps.location?.let {
            onNavigateToWeatherByLocation(it.id)
        }
    }

    val topBarPadding by animateDpAsState(
        targetValue = if (searchBarOnTop) 0.dp else with(density) { (screenHeight.toPx() / 2.5f).toDp() },
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    )

    TopTitleText(
        modifier = Modifier.padding(top = screenHeight / 4),
        visible = !searchBarOnTop,
        title = stringResource(R.string.use_search_title),
        maxLines = 2
    )

    Column {
        SearchBar(
            modifier = Modifier
                .padding(top = topBarPadding),
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
        } else FavoritesLocations(uiState.favorites, uiState.favoritesForecast, pushAction, onNavigateToWeatherByLocation)

        if (gpsModeEnabled) {
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
        if (uiState.gps.isPermissionGranted && gpsModeEnabled) {
            GpsModeAlertDialog(
                onDismissRequest = { gpsModeEnabled = false },
                onGpsModeEnabled = { enabled ->
                    pushAction(HomeScreenAction.DeviceGpsModeEnabled(enabled))
                }
            )
        }
    }

    BottomTitleText(
        modifier = Modifier.padding(bottom = screenHeight / 4),
        visible = !searchBarOnTop,
        title = stringResource(R.string.use_gps_title),
        maxLines = 2
    )
}

@Composable
private fun BoxScope.TopTitleText(
    modifier: Modifier = Modifier,
    visible: Boolean,
    enter: EnterTransition = expandVertically(
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    ),
    exit: ExitTransition = shrinkVertically(
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    ),
    title: String,
    maxLines: Int,
) {
    AnimatedAutoSizeTitleText(
        modifier = modifier,
        visible = visible,
        enter = enter,
        exit = exit,
        title = title,
        maxLines = maxLines
    )
}

@Composable
private fun BoxScope.BottomTitleText(
    modifier: Modifier = Modifier,
    visible: Boolean,
    enter: EnterTransition = expandVertically(
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
        expandFrom = Alignment.Top
    ),
    exit: ExitTransition = shrinkVertically(
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION
        ),
        shrinkTowards = Alignment.Top
    ),
    title: String,
    textAlign: TextAlign = TextAlign.End,
    maxLines: Int,
) {
    AnimatedAutoSizeTitleText(
        modifier = modifier,
        alignment = Alignment.BottomEnd,
        visible = visible,
        enter = enter,
        exit = exit,
        title = title,
        textAlign = textAlign,
        maxLines = maxLines
    )
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
        TopBar(modifier, inputLocation, pushAction, isSearching, isSearchMode, onNavigateToAppSettings)
        GpsButton(isSearchMode, onGpsClickButton)
    }

}

@Composable
private fun GpsButton(
    isSearchMode: Boolean,
    onGetGpsClickButton: (Boolean) -> Unit,
) {
    AnimatedCircleButton(isSearchMode, onGetGpsClickButton, ANIMATION_DURATION)
}

@Composable
private fun TopBar(
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

    Column(
        modifier = Modifier
            .padding(horizontal = padding16)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        locations.forEach { location ->
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
                            location.city?.let {
                                append("${it}, ")
                            } ?: ""
                        }
                        withStyle(
                            MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            )
                        ) {
                            location.state?.let {
                                append("${it}, ")
                            } ?: ""
                            location.country?.let {
                                append(it)
                            } ?: ""
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
                        if (favorites.any { it.id == location.id }) {
                            pushAction(HomeScreenAction.RemoveFavorite(location.id))
                        } else {
                            pushAction(HomeScreenAction.AddFavorite(location.id))
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (favorites.any { it.id == location.id }) WeatherIcons.RemoveFavorite else WeatherIcons.AddFavorite,
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
        LocationApiLink(
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
    favorites: ImmutableList<Location>,
    favoritesForecast: ImmutableMap<Int, Forecast>,
    pushAction: (HomeScreenAction) -> Unit = {},
    onNavigateToWeatherByLocation: (Int) -> Unit,
) {
    val drawableBackgroundRes = remember(favorites) {
        mapOf(
            CompactWeatherCondition.CLEAR to R.drawable.clear,
            CompactWeatherCondition.PARTLY_CLOUDY to R.drawable.partly_cloud,
            CompactWeatherCondition.CLOUDY to R.drawable.overcast,
            CompactWeatherCondition.FOG to R.drawable.fog,
            CompactWeatherCondition.RAIN to R.drawable.rain,
            CompactWeatherCondition.SNOW to R.drawable.snow,
            CompactWeatherCondition.THUNDER to R.drawable.thunder,
        )
    }

    val currentTime by produceState(initialValue = Clock.System.now()) {
        while (true) {
            value = Clock.System.now()
            delay(60_000L)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = padding16)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space16),
    ) {
        items(favorites, key = { location -> location.id }) { location ->
            val favoriteForecast = favoritesForecast[location.id]

            favoriteForecast?.let {
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
                    backgroundResIdByCondition = drawableBackgroundRes.getValue(it.today.condition),
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
                    .height(space16)
            )
        }
    }
}

@Composable
private fun LocationApiLink(modifier: Modifier, url: String, description: String) {
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
    val uiState = HomeScreenState(
        matchingLocations = listOf(
            Location.EMPTY.copy(
                city = "Москва",
                state = "Московская область",
                country = "Россия"
            ),
            Location.EMPTY.copy(
                city = "Москоу",
                state = "Айдахо",
                country = "США"
            )
        ).toPersistentList(),
        favorites = listOf(
            Location.EMPTY.copy(
                city = "Москва",
                state = "Московская область",
                country = "Россия"
            ),
            Location.EMPTY.copy(
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
                uiState,
                eventFlow = flowOf(),
                snackBarHostState = remember { SnackbarHostState() }
            )
        }

    }
}