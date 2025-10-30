package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.core.ui.utils.handleHorizontalDragEnd
import com.contraomnese.weather.core.ui.utils.toPx
import com.contraomnese.weather.core.ui.widgets.ImageBackgroundWithGradient
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.TitleSection
import com.contraomnese.weather.core.ui.widgets.WeatherBottomBar
import com.contraomnese.weather.core.ui.widgets.WeatherSnackBarHost
import com.contraomnese.weather.design.theme.itemHeight150
import com.contraomnese.weather.design.theme.itemHeight300
import com.contraomnese.weather.design.theme.itemHeight32
import com.contraomnese.weather.design.theme.itemWidth64
import com.contraomnese.weather.design.theme.padding32
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.presentation.utils.handleError
import com.contraomnese.weather.weatherByLocation.presentation.sections.AqiSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.DailyForecastSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.HourlyForecastSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.HumiditySection
import com.contraomnese.weather.weatherByLocation.presentation.sections.PressureSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.RainfallSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.SunriseSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.UVIndexSection
import com.contraomnese.weather.weatherByLocation.presentation.sections.WindSection
import kotlinx.coroutines.flow.Flow
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
internal fun WeatherRoute(
    viewModel: WeatherViewModel,
    eventFlow: Flow<WeatherScreenEvent>,
    pushAction: (WeatherScreenAction) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val snackBarHostState = LocalSnackbarHostState.current

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    eventFlow.collectEvent { event ->
        when (event) {
            is WeatherScreenEvent.NavigateToHome -> onNavigateToHome()
            is WeatherScreenEvent.HandleError -> snackBarHostState.showSnackbar(
                message = event.cause.handleError(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    val backgroundModifier = if (uiState.isLoading) Modifier.background(MaterialTheme.colorScheme.background) else Modifier
    val currentFavoriteIndex = uiState.favorites.indexOfFirst { it.id == uiState.locationId }

    Scaffold(
        modifier = Modifier.then(backgroundModifier),
        snackbarHost = { WeatherSnackBarHost(snackBarHostState) },
        bottomBar = {
            WeatherBottomBar(
                favorites = uiState.favorites.map { it.id },
                currentFavoriteIndex = currentFavoriteIndex,
                onHomeButtonClicked = onNavigateToHome,
            ) {
                pushAction(WeatherScreenAction.AddFavorite(uiState.locationId))
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when {
                uiState.isLoading -> LoadingIndicator(
                    Modifier
                        .align(Alignment.Center)
                        .width(itemWidth64)
                )

                else -> {
                    val coroutineScope = rememberCoroutineScope()
                    var offsetX by remember { mutableFloatStateOf(0f) }
                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.toPx()

                    val scaleAnimated = remember { Animatable(1f) }
                    val alphaAnimated = remember { Animatable(1f) }

                    uiState.weather?.let {
                        AnimatedBackground(condition = it.today.condition)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(currentFavoriteIndex) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        handleHorizontalDragEnd(
                                            offset = offsetX,
                                            onOffsetChange = { offsetX = it },
                                            screenWidth = screenWidth,
                                            currentFavoriteIndex = currentFavoriteIndex,
                                            lastFavoriteIndex = uiState.favorites.lastIndex,
                                            coroutineScope = coroutineScope,
                                            scaleAnimated = scaleAnimated,
                                            alphaAnimated = alphaAnimated,
                                            onDragNext = { pushAction(WeatherScreenAction.SwapFavorite(currentFavoriteIndex + 1)) },
                                            onDragPrev = { pushAction(WeatherScreenAction.SwapFavorite(currentFavoriteIndex - 1)) }
                                        )
                                    },
                                    onHorizontalDrag = { _, dragAmount ->
                                        offsetX += dragAmount
                                    }
                                )
                            }
                    ) {
                        key(currentFavoriteIndex) {

                            requireNotNull(uiState.weather)
                            requireNotNull(uiState.appSettings)

                            WeatherScreen(
                                weather = requireNotNull(uiState.weather),
                                appSettings = requireNotNull(uiState.appSettings),
                                modifier = Modifier
                                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                                    .graphicsLayer {
                                        scaleX = scaleAnimated.value
                                        scaleY = scaleAnimated.value
                                        alpha = alphaAnimated.value
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun WeatherScreen(
    modifier: Modifier = Modifier,
    weather: Forecast,
    appSettings: AppSettings,
) {

    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val minTitleBoxHeightPx = itemHeight150.toPx()
    val maxTitleBoxHeightPx = itemHeight300.toPx()

    var currentTitleBoxHeight by remember { mutableFloatStateOf(maxTitleBoxHeightPx) }

    val titleBoxHeightProgress by remember(currentTitleBoxHeight) {
        derivedStateOf {
            ((currentTitleBoxHeight - minTitleBoxHeightPx) /
                    (maxTitleBoxHeightPx - minTitleBoxHeightPx))
                .coerceIn(0f, 1f)
        }
    }

    val sectionVerticalSpacing = space8
    val headerSectionHeight = itemHeight32
    val lazyListFooterHeight = itemHeight32

    val maxVisibleSectionOffset = headerSectionHeight.toPx() + sectionVerticalSpacing.toPx()

    val sections = remember {
        mutableStateListOf(
            HourlyForecastSection(),
            DailyForecastSection(),
            AqiSection(),
            SunriseSection(isDay = weather.today.isDay),
            UVIndexSection(),
            WindSection(),
            HumiditySection(),
            RainfallSection(),
            PressureSection()
        )
    }

    val nestedScrollConnection by remember {
        mutableStateOf(
            object : NestedScrollConnection {

                private fun isLastItemVisible(): Boolean {
                    val lastItem = lazyListState.layoutInfo.visibleItemsInfo.last()
                    val isLastItemVisible = lastItem.index == sections.size &&
                            (lastItem.offset + lastItem.size) <= lazyListState.layoutInfo.viewportEndOffset

                    return isLastItemVisible
                }

                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                    if (available.y == 0f) return Offset.Zero

                    if (isLastItemVisible() && available.y < 0) {
                        return Offset(0f, available.y)
                    }
                    // end block

                    var consumedResult = 0f
                    var availableScrollResult = available.y
                    val currentVisibleSection = lazyListState.layoutInfo.visibleItemsInfo.first()
                    val nextSection = lazyListState.layoutInfo.visibleItemsInfo.getOrNull(1)
                    val lastSection = lazyListState.layoutInfo.visibleItemsInfo.last()

                    // steal scroll to change title height
                    if (availableScrollResult < 0 && currentVisibleSection.index == 0) {
                        val prevTitleBoxHeight = currentTitleBoxHeight
                        currentTitleBoxHeight =
                            (currentTitleBoxHeight + availableScrollResult).coerceIn(
                                minTitleBoxHeightPx,
                                maxTitleBoxHeightPx
                            )
                        val consumedByTitle = currentTitleBoxHeight - prevTitleBoxHeight
                        consumedResult += consumedByTitle
                        availableScrollResult -= consumedByTitle
                    }

                    if (availableScrollResult == 0f) {
                        return Offset(0f, consumedResult)
                    }
                    // end block

                    // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                    if (availableScrollResult > 0f && currentVisibleSection.offset < 0) {
                        return if (availableScrollResult + currentVisibleSection.offset > 0) {
                            val exactOffsetConsumed = availableScrollResult + currentVisibleSection.offset
                            Offset(0f, exactOffsetConsumed)
                        } else {
                            Offset(0f, consumedResult)
                        }
                    }

                    // checking that current container's body is expanded
                    val (currentBodyHeight, maxBodyHeight) = sections[currentVisibleSection.index]
                    currentBodyHeight?.let {

                        val minBodyHeight = if (available.y < 0) {
                            if (it.value == 0f) {
                                0f
                            } else if (lastSection.index == sections.size - 1) {
                                val overlap =
                                    (lastSection.size - (lazyListState.layoutInfo.viewportEndOffset - lastSection.offset) +
                                            lazyListState.layoutInfo.mainAxisItemSpacing + with(density) { lazyListFooterHeight.toPx() })

                                (it.value - overlap).coerceAtLeast(0f)

                            } else if ((lastSection.index > sections.size - 1)) {
                                val overlap =
                                    (lastSection.size - (lazyListState.layoutInfo.viewportEndOffset - lastSection.offset)).toFloat()
                                (it.value - overlap).coerceAtLeast(0f)
                            } else 0f
                        } else 0f

                        val newBodyHeight = (it.value + availableScrollResult).coerceIn(minBodyHeight, maxBodyHeight)
                        val consumedScrollYByBody = newBodyHeight - it.value
                        sections[currentVisibleSection.index].bodyHeight?.value = newBodyHeight
                        consumedResult += consumedScrollYByBody
                        availableScrollResult -= consumedScrollYByBody

                        // make sure we don't steal more scroll than needed to show a new container when scrolling UP
                        nextSection?.let { nextSection ->
                            if (newBodyHeight == 0f && availableScrollResult < 0 && abs(available.y) > nextSection.offset) {
                                val exactOffsetConsumed = consumedResult + availableScrollResult + nextSection.offset
                                return Offset(0f, exactOffsetConsumed)
                            }
                        }

                        // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                        if (newBodyHeight == maxBodyHeight && (currentVisibleSection.offset + availableScrollResult) > maxVisibleSectionOffset) {
                            val exactOffsetConsumed =
                                currentVisibleSection.offset + availableScrollResult + consumedResult - maxVisibleSectionOffset
                            return Offset(0f, exactOffsetConsumed)
                        }
                    }

                    // we give away the remaining scroll
                    return Offset(0f, consumedResult)
                }

                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    val availableScroll = available.y
                    val currentVisibleSection = lazyListState.layoutInfo.visibleItemsInfo.first()
                    val firstSection = sections.first()
                    if (firstSection.bodyHeight?.value != firstSection.bodyMaxHeight) return Offset(0f, availableScroll)
                    // steal scroll to change title height
                    if (availableScroll > 0 && currentVisibleSection.index == 0) {
                        val prevTitleBoxHeight = currentTitleBoxHeight
                        currentTitleBoxHeight =
                            (currentTitleBoxHeight + availableScroll).coerceIn(minTitleBoxHeightPx, maxTitleBoxHeightPx)
                        return Offset(0f, currentTitleBoxHeight - prevTitleBoxHeight)
                    }
                    // end title height changing
                    return Offset.Zero
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = padding32)
    ) {
        TitleSection(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { currentTitleBoxHeight.toDp() }),
            location = weather.location.city,
            currentTemp = weather.today.temperature,
            feelsLikeTemp = weather.today.feelsLikeTemperature,
            maxTemp = weather.forecast.today.maxTemperature,
            minTemp = weather.forecast.today.minTemperature,
            condition = weather.today.conditionText,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding8)
                .nestedScroll(nestedScrollConnection),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(sectionVerticalSpacing),
        ) {
            items(sections) { section ->

                val measureContainerHeight: (Int) -> Unit = { measureBodyHeight ->
                    if (section.bodyHeight == null) {
                        val currentSectionIndex = sections.indexOf(section)
                        if (currentSectionIndex >= 0) {
                            sections[currentSectionIndex] = sections[currentSectionIndex].initBodyHeight(measureBodyHeight.toFloat())
                        }
                    }
                }

                section.Render(
                    headerSectionHeight = headerSectionHeight,
                    weather = weather,
                    appSettings = appSettings,
                    measureContainerHeight = measureContainerHeight,
                    progress = titleBoxHeightProgress
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(lazyListFooterHeight)
                        .background(MaterialTheme.colorScheme.error)
                )
            }
        }
    }
}

@Composable
private fun AnimatedBackground(condition: CompactWeatherCondition) {
    Crossfade(
        targetState = condition,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "WeatherBackground"
    ) { targetCondition ->
        ImageBackgroundWithGradient(condition = targetCondition)
    }
}

