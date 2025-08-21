package com.contraomnese.weather.weatherByLocation.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.CollapsableContainerWithAnimatedHeader
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space32
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.GeoLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherDomainModel
import com.contraomnese.weather.weatherByLocation.presentation.data.WeatherSection
import com.contraomnese.weather.weatherByLocation.presentation.data.WeatherSectionType
import com.contraomnese.weather.weatherByLocation.presentation.data.mapSections
import com.contraomnese.weather.weatherByLocation.presentation.data.sectionsList
import kotlinx.coroutines.flow.map
import kotlin.math.abs

@Composable
internal fun WeatherRoute(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingIndicator(
                modifier = Modifier
                    .height(itemHeight64)
                    .align(Alignment.Center)
            )

            else -> WeatherScreen(
                uiState = uiState, modifier = modifier
            )
        }
    }
}

@Composable
private fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
) {

    val lazyListState = rememberLazyListState()

    val minTitleBoxHeightPx = 100.dp.toPx()
    val maxTitleBoxHeightPx = 300.dp.toPx()
    var currentTitleBoxHeight by remember { mutableFloatStateOf(maxTitleBoxHeightPx) }

    val progress by remember(currentTitleBoxHeight, minTitleBoxHeightPx, maxTitleBoxHeightPx) {
        derivedStateOf {
            ((currentTitleBoxHeight - minTitleBoxHeightPx) / (maxTitleBoxHeightPx - currentTitleBoxHeight))
                .coerceIn(0f, 1f)
        }
    }

    Log.d("123", "Progress: $progress")
    Log.d("123", "currentTitleBoxHeight: $currentTitleBoxHeight")

    val sectionVerticalSpacing = 12.dp
    val headerSectionHeight = 40.dp
    val dailySection = WeatherSection.DailyForecastSection(400.dp.toPx())
    val hourlySection = WeatherSection.HourlyForecastSection(700.dp.toPx())
    val uvIndexSection = WeatherSection.UVIndexSection(200.dp.toPx())
    val sunriseSection = WeatherSection.SunriseSection(200.dp.toPx())

    val maxVisibleSectionOffset = headerSectionHeight.toPx() + sectionVerticalSpacing.toPx()
    var currentVisibleSectionIndex by remember { mutableIntStateOf(-1) }

    var sections by remember {
        mutableStateOf(
            listOf(
                WeatherSectionType.Solo(hourlySection),
                WeatherSectionType.Solo(dailySection),
                WeatherSectionType.InRow(uvIndexSection, sunriseSection),
            )
        )
    }

    val nestedScrollConnection = remember {
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

                val flatSections = sections.flatMap { it.sectionsList() }
                // checking that all container's bodies after current container are expanded
                if (availableScrollResult > 0) {
                    // плоский список секций
                    flatSections.drop(currentVisibleSectionIndex + 1)
                        .forEachIndexed { nextIndex, section ->
                            val (bodyHeight, maxHeight) = section
                            if (bodyHeight != maxHeight) {
                                val newHeight = (bodyHeight + availableScrollResult).coerceIn(0f, maxHeight)

                                sections = sections.mapSections { globalIndex, sec ->
                                    val targetIdx = nextIndex + currentVisibleSectionIndex + 1
                                    if (globalIndex == targetIdx) {
                                        when (sec) {
                                            is WeatherSection.HourlyForecastSection -> sec.copy(bodyHeight = newHeight)
                                            is WeatherSection.DailyForecastSection -> sec.copy(bodyHeight = newHeight)
                                            is WeatherSection.UVIndexSection -> sec.copy(bodyHeight = newHeight)
                                            is WeatherSection.SunriseSection -> sec.copy(bodyHeight = newHeight)
                                        }
                                    } else sec
                                }

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
                        Offset(0f, exactOffsetConsumed)
                    } else {
                        Offset(0f, consumedResult)
                    }
                }

                val nextCollapseContainer =
                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentVisibleSectionIndex + 1 }

                val nextCollapseContainerOffset = nextCollapseContainer?.offset?.toFloat() ?: Float.POSITIVE_INFINITY

                // checking that current container's body is expanded
                val (currentBodyHeight, maxBodyHeight) = flatSections[currentVisibleSectionIndex]
                val newBodyHeight = (currentBodyHeight + availableScrollResult).coerceIn(0f, maxBodyHeight)
                sections = sections.mapSections { globalIndex, sec ->
                    if (globalIndex == currentVisibleSectionIndex) {
                        when (sec) {
                            is WeatherSection.HourlyForecastSection -> sec.copy(bodyHeight = newBodyHeight)
                            is WeatherSection.DailyForecastSection -> sec.copy(bodyHeight = newBodyHeight)
                            is WeatherSection.UVIndexSection -> sec.copy(bodyHeight = newBodyHeight)
                            is WeatherSection.SunriseSection -> sec.copy(bodyHeight = newBodyHeight)
                        }
                    } else sec
                }
                val consumedScrollYByBody = newBodyHeight - currentBodyHeight
                consumedResult += consumedScrollYByBody
                availableScrollResult -= consumedScrollYByBody

                // make sure we don't steal more scroll than needed to show a new container when scrolling UP
                if (newBodyHeight == 0f && availableScrollResult < 0 && abs(availableScrollResult + consumedResult) > nextCollapseContainerOffset) {
                    val exactOffsetConsumed = availableScrollResult + consumedResult + nextCollapseContainerOffset
                    return Offset(0f, exactOffsetConsumed)
                }
                // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                if (newBodyHeight == maxBodyHeight && (currentCollapseContainerOffset + availableScrollResult) > maxVisibleSectionOffset) {
                    val exactOffsetConsumed =
                        availableScrollResult + consumedResult - maxVisibleSectionOffset

                    // for some reason when offset = 0 the next index does not take the correct value
                    currentVisibleSectionIndex--
                    return Offset(0f, exactOffsetConsumed)
                }
                // we give away the remaining scroll
                return Offset(0f, consumedResult)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val availableScroll = available.y
                // steal scroll to change title height
                if (availableScroll > 0 && lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0) {
                    val prevTitleBoxHeight = currentTitleBoxHeight
                    currentTitleBoxHeight =
                        (currentTitleBoxHeight + availableScroll).coerceIn(minTitleBoxHeightPx, maxTitleBoxHeightPx)
                    return Offset(0f, currentTitleBoxHeight - prevTitleBoxHeight)
                }
                // end title height changing
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .map { vis -> vis.firstOrNull { it.offset >= -maxVisibleSectionOffset }?.index ?: -1 }
            .collect { currentVisibleSectionIndex = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TitleSection(
            currentTitleBoxHeight,
            location = uiState.location.name,
            currentTemp = uiState.weather?.currentTemperature ?: "?",
            maxTemp = uiState.weather?.maxTemperature ?: "?",
            minTemp = uiState.weather?.minTemperature ?: "?",
            condition = uiState.weather?.condition ?: "?",
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding8, vertical = padding8)
                .nestedScroll(nestedScrollConnection),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(sectionVerticalSpacing),
        ) {
            items(sections) { sectionType ->
                when (sectionType) {

                    is WeatherSectionType.Solo,
                        -> when (sectionType.section) {
                        is WeatherSection.DailyForecastSection,
                            -> CollapsableContainer(
                            headerHeight = headerSectionHeight,
                            currentBodyHeight = sectionType.section.bodyHeight,
                            maxBodyHeight = sectionType.section.bodyMaxHeight,
                        ) {
                            repeat(7) { Text("Body$it") }
                        }

                        is WeatherSection.HourlyForecastSection,
                            -> CollapsableContainerWithAnimatedHeader(
                            headerHeight = headerSectionHeight,
                            currentBodyHeight = sectionType.section.bodyHeight,
                            maxBodyHeight = sectionType.section.bodyMaxHeight,
                            progress = progress
                        ) {
                            repeat(8) { Text("Body$it") }
                        }

                        is WeatherSection.SunriseSection, is WeatherSection.UVIndexSection -> Unit
                    }

                    is WeatherSectionType.InRow,
                        -> Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space16)
                    ) {
                        CollapsableContainer(
                            headerHeight = headerSectionHeight,
                            currentBodyHeight = sectionType.firstSection.bodyHeight,
                            maxBodyHeight = sectionType.firstSection.bodyMaxHeight,
                            modifier = Modifier.weight(1f)
                        ) {
                            repeat(5) { Text("Body$it") }
                        }
                        CollapsableContainer(
                            headerHeight = headerSectionHeight,
                            currentBodyHeight = sectionType.secondSection.bodyHeight,
                            maxBodyHeight = sectionType.secondSection.bodyMaxHeight,
                            modifier = Modifier.weight(1f)
                        ) {
                            repeat(5) { Text("Body$it") }
                        }
                    }
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
private fun TitleSection(
    height: Float,
    location: String,
    currentTemp: String,
    maxTemp: String,
    minTemp: String,
    condition: String,
    modifier: Modifier = Modifier,
) {

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { height.toDp() })
            .padding(horizontal = padding8)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = .0f)),
        contentAlignment = Alignment.Center
    ) {
        if (maxHeight > 250.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(padding8)
            ) {
                Text(
                    location, style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.current_temperature_title, currentTemp),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    condition, style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.max_min_temperature_title, maxTemp, minTemp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(padding8)
            ) {
                Text(
                    location, style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.temperature_and_condition_title, currentTemp, condition),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        WeatherScreen(
            uiState = WeatherUiState(
                isLoading = false, location = GeoLocationDomainModel(
                    id = 7940,
                    name = "Maura Wilcox",
                    point = CoordinatesDomainModel(
                        latitude = LatitudeDomainModel(
                            value = 4.5
                        ), longitude = LongitudeDomainModel(value = 6.7)
                    )
                ), weather = WeatherDomainModel(
                    currentTemperature = "33",
                    maxTemperature = "34",
                    minTemperature = "35",
                    condition = "Ясно"
                )
            ),
        )
    }
}