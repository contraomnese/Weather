package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.utils.toPx
import com.contraomnese.weather.core.ui.widgets.AirQualityItem
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.CollapsableContainerWithAnimatedHeader
import com.contraomnese.weather.core.ui.widgets.ForecastDailyColumn
import com.contraomnese.weather.core.ui.widgets.ForecastHourlyLazyRow
import com.contraomnese.weather.core.ui.widgets.HumidityItem
import com.contraomnese.weather.core.ui.widgets.ImageBackgroundWithGradient
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.PressureItem
import com.contraomnese.weather.core.ui.widgets.RainfallItem
import com.contraomnese.weather.core.ui.widgets.SunriseItem
import com.contraomnese.weather.core.ui.widgets.TitleSection
import com.contraomnese.weather.core.ui.widgets.UvIndexItem
import com.contraomnese.weather.core.ui.widgets.WindItem
import com.contraomnese.weather.design.theme.itemHeight150
import com.contraomnese.weather.design.theme.itemHeight300
import com.contraomnese.weather.design.theme.itemHeight32
import com.contraomnese.weather.design.theme.itemWidth64
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding32
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space32
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.weatherByLocation.presentation.data.AqiSection
import com.contraomnese.weather.weatherByLocation.presentation.data.DailyForecastSection
import com.contraomnese.weather.weatherByLocation.presentation.data.HourlyForecastSection
import com.contraomnese.weather.weatherByLocation.presentation.data.HumiditySection
import com.contraomnese.weather.weatherByLocation.presentation.data.PressureSection
import com.contraomnese.weather.weatherByLocation.presentation.data.RainfallSection
import com.contraomnese.weather.weatherByLocation.presentation.data.SunriseSection
import com.contraomnese.weather.weatherByLocation.presentation.data.UVIndexSection
import com.contraomnese.weather.weatherByLocation.presentation.data.WeatherSection
import com.contraomnese.weather.weatherByLocation.presentation.data.WindSection
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
internal fun WeatherRoute(
    viewModel: WeatherViewModel,
    eventFlow: Flow<WeatherScreenEvent>,
    pushAction: (WeatherScreenAction) -> Unit,
) {

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    eventFlow.collectEvent { event ->
        when (event) {
            is WeatherScreenEvent.NavigateToBack -> TODO()
            is WeatherScreenEvent.ShowError -> TODO()
            is WeatherScreenEvent.SwapFavoriteGetWeather -> pushAction(WeatherScreenAction.SwapFavorite(event.index))
        }
    }

    val backgroundModifier = if (uiState.isLoading) Modifier.background(MaterialTheme.colorScheme.background) else Modifier

    val coroutineScope = rememberCoroutineScope()
    var offsetX by remember { mutableFloatStateOf(0f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.toPx()

    val scaleAnimated = remember { Animatable(1f) }
    val alphaAnimated = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(backgroundModifier)
    ) {

        if (!uiState.isLoading) {

            val favoriteIndex = uiState.favorites.indexOfFirst { it.id == uiState.locationId }

            AnimatedBackground(condition = uiState.weather!!.today.condition)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(favoriteIndex) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                when {
                                    offsetX > screenWidth * 0.25f && favoriteIndex > 0 -> {
                                        val prevFavoriteIndex = favoriteIndex - 1
                                        pushAction(WeatherScreenAction.SwapFavorite(prevFavoriteIndex))
                                        AnimateMove(coroutineScope, scaleAnimated, alphaAnimated)
                                    }

                                    offsetX < -screenWidth * 0.25f && favoriteIndex < uiState.favorites.lastIndex -> {
                                        val nextFavoriteIndex = favoriteIndex + 1
                                        pushAction(WeatherScreenAction.SwapFavorite(nextFavoriteIndex))
                                        AnimateMove(coroutineScope, scaleAnimated, alphaAnimated)
                                    }
                                }
                                coroutineScope.launch {
                                    animate(
                                        initialValue = offsetX,
                                        targetValue = 0f,
                                        animationSpec = tween(600)
                                    ) { value, _ -> offsetX = value }
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                offsetX += dragAmount
                            }
                        )
                    }
            ) {
                key(favoriteIndex) {
                    WeatherScreen(
                        uiState = uiState,
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
        } else {
            LoadingIndicator(
                Modifier
                    .align(Alignment.Center)
                    .width(itemWidth64)
            )
        }
    }
}

@Composable
internal fun WeatherScreen(
    modifier: Modifier = Modifier,
    uiState: WeatherScreenState,
) {
    requireNotNull(uiState.weather)
    requireNotNull(uiState.appSettings)

    val lazyListState = rememberLazyListState()

    val minTitleBoxHeightPx = itemHeight150.toPx()
    val maxTitleBoxHeightPx = itemHeight300.toPx()

    var currentTitleBoxHeight by remember { mutableFloatStateOf(maxTitleBoxHeightPx) }

    val progress by remember(currentTitleBoxHeight) {
        derivedStateOf {
            ((currentTitleBoxHeight - minTitleBoxHeightPx) /
                    (maxTitleBoxHeightPx - minTitleBoxHeightPx))
                .coerceIn(0f, 1f)
        }
    }

    val sectionVerticalSpacing = space8
    val headerSectionHeight = itemHeight32

    val maxVisibleSectionOffset = headerSectionHeight.toPx() + sectionVerticalSpacing.toPx()
    var currentVisibleSectionIndex by remember { mutableIntStateOf(0) }

    var sections by remember {
        mutableStateOf(
            persistentListOf(
                HourlyForecastSection(),
                DailyForecastSection(),
                AqiSection(),
                SunriseSection(isDay = uiState.weather.today.isDay),
                UVIndexSection(),
                WindSection(),
                HumiditySection(),
                RainfallSection(),
                PressureSection()
            )
        )
    }

    val nestedScrollConnection by remember {
        mutableStateOf(
            object : NestedScrollConnection {

                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                    if (available.y == 0f) return Offset.Zero

                    // disable scroll when last item is visible
                    val lastItem = lazyListState.layoutInfo.let {
                        it.visibleItemsInfo.firstOrNull { lastItem ->
                            lastItem.index == sections.size &&
                                    lastItem.offset + lastItem.size > it.viewportEndOffset
                        }
                    }
                    if (lastItem != null && available.y < 0) {
                        return Offset(0f, available.y)
                    }
                    // end block

                    var consumedResult = 0f
                    var availableScrollResult = available.y

                    // steal scroll to change title height
                    if (availableScrollResult < 0 && currentVisibleSectionIndex == 0) {
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

                    // collapsableContainer height changing
                    val currentCollapseContainer =
                        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentVisibleSectionIndex }

                    val currentCollapseContainerOffset =
                        currentCollapseContainer?.offset?.toFloat() ?: Float.NEGATIVE_INFINITY

                    // checking that all container's bodies after current container are expanded
                    if (availableScrollResult > 0) {
                        sections.drop(currentVisibleSectionIndex + 1)
                            .forEachIndexed { nextIndex, section ->
                                val (bodyHeight, maxHeight) = section
                                if (bodyHeight != maxHeight) {
                                    val newHeight = (bodyHeight!! + availableScrollResult).coerceIn(0f, maxHeight)

                                    sections = sections.mapIndexed { idx, sec ->
                                        val targetIdx = nextIndex + currentVisibleSectionIndex + 1
                                        if (idx == targetIdx) {
                                            sec.copyWithBodyHeight(newHeight)
                                        } else sec
                                    }.toPersistentList()
                                    val consumedScrollYByBody = newHeight - bodyHeight
                                    consumedResult += consumedScrollYByBody
                                    availableScrollResult -= consumedScrollYByBody

                                    if (newHeight == maxHeight && availableScrollResult > maxVisibleSectionOffset) {
                                        return Offset(0f, availableScrollResult - maxVisibleSectionOffset)
                                    }

                                    return@forEachIndexed
                                }
                            }
                    }


                    if (availableScrollResult == 0f) {
                        return Offset(0f, consumedResult)
                    }
                    // end block

                    // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                    if (availableScrollResult > 0f && currentCollapseContainerOffset < 0) {
                        return if (availableScrollResult + currentCollapseContainerOffset > 0) {
                            val exactOffsetConsumed = availableScrollResult + currentCollapseContainerOffset
                            if (currentVisibleSectionIndex > 0) currentVisibleSectionIndex--
                            Offset(0f, exactOffsetConsumed)
                        } else {
                            Offset(0f, consumedResult)
                        }
                    }

                    val nextCollapseContainer =
                        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentVisibleSectionIndex + 1 }

                    val nextCollapseContainerOffset = nextCollapseContainer?.offset?.toFloat() ?: Float.POSITIVE_INFINITY

                    // checking that current container's body is expanded
                    val (currentBodyHeight, maxBodyHeight) = sections[currentVisibleSectionIndex]
                    val newBodyHeight = (currentBodyHeight!! + availableScrollResult).coerceIn(0f, maxBodyHeight)
                    sections = sections.mapIndexed { idx, type ->
                        if (idx == currentVisibleSectionIndex) {
                            type.copyWithBodyHeight(newBodyHeight)
                        } else type
                    }.toPersistentList()
                    val consumedScrollYByBody = newBodyHeight - currentBodyHeight
                    consumedResult += consumedScrollYByBody
                    availableScrollResult -= consumedScrollYByBody

                    // make sure we don't steal more scroll than needed to show a new container when scrolling UP
                    if (newBodyHeight == 0f && availableScrollResult < 0 && abs(availableScrollResult + consumedResult) > nextCollapseContainerOffset) {
                        val exactOffsetConsumed = consumedResult + availableScrollResult + nextCollapseContainerOffset
                        if (currentVisibleSectionIndex < sections.size - 1) currentVisibleSectionIndex++
                        return Offset(0f, exactOffsetConsumed)
                    }
                    // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                    if (newBodyHeight == maxBodyHeight && (currentCollapseContainerOffset + availableScrollResult) > maxVisibleSectionOffset) {
                        val exactOffsetConsumed =
                            currentCollapseContainerOffset + availableScrollResult + consumedResult - maxVisibleSectionOffset
                        if (currentVisibleSectionIndex > 0) currentVisibleSectionIndex--
                        return Offset(0f, exactOffsetConsumed)
                    }
                    // we give away the remaining scroll
                    return Offset(0f, consumedResult)
                }

                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    val availableScroll = available.y

                    val firstSection = sections.first()
                    if (firstSection.bodyHeight != firstSection.bodyMaxHeight) return Offset(0f, availableScroll)
                    // steal scroll to change title height
                    if (availableScroll > 0 && currentVisibleSectionIndex == 0) {
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
            location = uiState.weather.location.city,
            currentTemp = uiState.weather.today.temperature,
            feelsLikeTemp = uiState.weather.today.feelsLikeTemperature,
            maxTemp = uiState.weather.forecast.today.maxTemperature,
            minTemp = uiState.weather.forecast.today.minTemperature,
            condition = uiState.weather.today.conditionText,
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

                val measureContainerHeight: (Int) -> Unit = {
                    if (section.bodyHeight == null) {
                        sections =
                            sections.map { sec -> if (section == sec) sec.copyWithBodyHeight(it.toFloat()) else sec }.toPersistentList()
                    }
                }

                when (section) {
                    is HourlyForecastSection,
                        -> HourlyForecastSection(headerSectionHeight, section, uiState.weather, progress, measureContainerHeight)

                    is DailyForecastSection,
                        -> DailyForecastSection(
                        headerSectionHeight,
                        section,
                        measureContainerHeight,
                        uiState.weather,
                        uiState.appSettings.temperatureUnit
                    )

                    is AqiSection,
                        -> AqiSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is SunriseSection,
                        -> SunriseSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is UVIndexSection,
                        -> UvIndexSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is WindSection,
                        -> WindSection(
                        headerSectionHeight,
                        section,
                        measureContainerHeight,
                        uiState.weather,
                        uiState.appSettings.windSpeedUnit
                    )

                    is HumiditySection,
                        -> HumiditySection(
                        headerSectionHeight,
                        section,
                        measureContainerHeight,
                        uiState.weather,
                        uiState.appSettings.temperatureUnit
                    )

                    is RainfallSection,
                        -> RainfallSection(
                        headerSectionHeight,
                        section,
                        measureContainerHeight,
                        uiState.weather,
                        uiState.appSettings.precipitationUnit
                    )

                    is PressureSection,
                        -> PressureSection(
                        headerSectionHeight,
                        section,
                        measureContainerHeight,
                        uiState.weather,
                        uiState.appSettings.pressureUnit
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(space32)
                        .background(MaterialTheme.colorScheme.error)
                )
            }
        }
    }
}

@Composable
private fun HourlyForecastSection(
    headerSectionHeight: Dp,
    section: HourlyForecastSection,
    weather: Forecast,
    progress: Float,
    measureContainerHeight: (Int) -> Unit,
) {
    CollapsableContainerWithAnimatedHeader(
        minHeaderHeight = headerSectionHeight,
        currentBodyHeight = section.bodyHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        alertTitle = weather.alerts.alerts.firstOrNull(),
        progress = progress,
        onContentMeasured = measureContainerHeight
    ) {
        ForecastHourlyLazyRow(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            items = weather.forecast.hours
        )
    }
}

@Composable
private fun DailyForecastSection(
    headerSectionHeight: Dp,
    section: DailyForecastSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
    temperatureUnit: TemperatureUnit,
) {
    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        ForecastDailyColumn(
            modifier = Modifier.padding(horizontal = padding16),
            items = weather.forecast.days,
            currentTemperature = weather.today.temperature.toInt(),
            temperatureUnit = temperatureUnit
        )
    }
}

@Composable
private fun AqiSection(
    headerSectionHeight: Dp,
    section: WeatherSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
) {
    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        AirQualityItem(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            aqiIndex = weather.today.airQuality.aqiIndex,
            coLevel = weather.today.airQuality.coLevel,
            no2Level = weather.today.airQuality.no2Level,
            o3Level = weather.today.airQuality.o3Level,
            so2Level = weather.today.airQuality.so2Level,
            pm25Level = weather.today.airQuality.pm25Level,
            pm10Level = weather.today.airQuality.pm10Level,
        )
    }
}

@Composable
private fun SunriseSection(
    headerSectionHeight: Dp,
    section: SunriseSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
) {
    val today = weather.forecast.today
    val location = weather.location

    val sunrise = today.sunrise
    val sunset = today.sunset
    val isDay = weather.today.isDay

    if (sunrise != null && sunset != null) {
        CollapsableContainer(
            headerHeight = headerSectionHeight,
            headerTitle = stringResource(section.title),
            headerIcon = section.icon,
            currentBodyHeight = section.bodyHeight,
            onContentMeasured = measureContainerHeight
        ) {
            SunriseItem(
                modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
                sunriseTime = sunrise,
                sunsetTime = sunset,
                timeZone = location.timeZone,
                isDay = isDay
            )
        }
    }
}

@Composable
private fun UvIndexSection(
    headerSectionHeight: Dp,
    section: UVIndexSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
) {
    val today = weather.today

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        UvIndexItem(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            index = today.uvIndex.value
        )
    }
}

@Composable
private fun WindSection(
    headerSectionHeight: Dp,
    section: WindSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
    windSpeedUnit: WindSpeedUnit,
) {
    val today = weather.today

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        WindItem(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            windSpeed = today.windSpeed,
            gustSpeed = today.gustSpeed,
            degree = today.windDegree,
            direction = today.windDirection,
            windSpeedUnit = windSpeedUnit
        )
    }
}

@Composable
private fun HumiditySection(
    headerSectionHeight: Dp,
    section: HumiditySection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
    temperatureUnit: TemperatureUnit,
) {
    val today = weather.today

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        HumidityItem(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            humidity = today.humidity,
            dewPoint = today.dewPoint,
            temperatureUnit = temperatureUnit
        )
    }
}

@Composable
private fun RainfallSection(
    headerSectionHeight: Dp,
    section: RainfallSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
    precipitationUnit: PrecipitationUnit,
) {
    val today = weather.today

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        RainfallItem(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            rainfallBeforeNow = today.rainfallBeforeNow,
            rainfallAfterNow = today.rainfallAfterNow,
            rainfallNow = today.rainfallNow,
            isRainingExpected = today.isRainingExpected,
            maxRainfall = today.maxRainfall,
            precipitationUnit = precipitationUnit
        )
    }
}

@Composable
private fun PressureSection(
    headerSectionHeight: Dp,
    section: PressureSection,
    measureContainerHeight: (Int) -> Unit,
    weather: Forecast,
    pressureUnit: PressureUnit,
) {
    val today = weather.today

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        PressureItem(
            modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
            value = today.pressure,
            pressureUnit = pressureUnit
        )
    }
}

@Composable
fun AnimatedBackground(condition: CompactWeatherCondition) {
    Crossfade(
        targetState = condition,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "WeatherBackground"
    ) { targetCondition ->
        ImageBackgroundWithGradient(condition = targetCondition)
    }
}

private fun AnimateMove(
    coroutineScope: CoroutineScope,
    scaleAnimatable: Animatable<Float, AnimationVector1D>,
    alphaAnimatable: Animatable<Float, AnimationVector1D>,
) {
    coroutineScope.launch {
        launch {
            scaleAnimatable.animateTo(0.8f, tween(300))
        }
        launch {
            alphaAnimatable.snapTo(0f)
        }
        launch {
            scaleAnimatable.animateTo(1f, tween(300))
            alphaAnimatable.animateTo(1f, tween(600))
        }
    }
}