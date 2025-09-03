package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.utils.toPx
import com.contraomnese.weather.core.ui.widgets.AirQualityItem
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.CollapsableContainerWithAnimatedHeader
import com.contraomnese.weather.core.ui.widgets.ForecastDailyColumn
import com.contraomnese.weather.core.ui.widgets.ForecastHourlyLazyRow
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.SunriseItem
import com.contraomnese.weather.core.ui.widgets.TitleSection
import com.contraomnese.weather.core.ui.widgets.UvIndexItem
import com.contraomnese.weather.core.ui.widgets.WindItem
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight240
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space32
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.GeoLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel
import com.contraomnese.weather.weatherByLocation.presentation.data.AqiSection
import com.contraomnese.weather.weatherByLocation.presentation.data.DailyForecastSection
import com.contraomnese.weather.weatherByLocation.presentation.data.HourlyForecastSection
import com.contraomnese.weather.weatherByLocation.presentation.data.HumiditySection
import com.contraomnese.weather.weatherByLocation.presentation.data.SunriseSection
import com.contraomnese.weather.weatherByLocation.presentation.data.UVIndexSection
import com.contraomnese.weather.weatherByLocation.presentation.data.WeatherSection
import com.contraomnese.weather.weatherByLocation.presentation.data.WindSection
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
                uiState = uiState
            )
        }
    }
}

@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
) {

    requireNotNull(uiState.weather)

    val lazyListState = rememberLazyListState()

    val minTitleBoxHeightPx = 150.dp.toPx()
    val maxTitleBoxHeightPx = 300.dp.toPx()
    var currentTitleBoxHeight by remember { mutableFloatStateOf(maxTitleBoxHeightPx) }

    val progress by remember(currentTitleBoxHeight, minTitleBoxHeightPx, maxTitleBoxHeightPx) {
        derivedStateOf {
            ((currentTitleBoxHeight - minTitleBoxHeightPx) / (maxTitleBoxHeightPx - currentTitleBoxHeight))
                .coerceIn(0f, 1f)
        }
    }

    val sectionVerticalSpacing = 10.dp
    val headerSectionHeight = 46.dp

    val maxVisibleSectionOffset = headerSectionHeight.toPx() + sectionVerticalSpacing.toPx()
    var currentVisibleSectionIndex by remember { mutableIntStateOf(0) }

    var sections by remember {
        mutableStateOf(
            persistentListOf(
                HourlyForecastSection(),
                DailyForecastSection(),
                AqiSection(),
                SunriseSection(),
                UVIndexSection(),
                WindSection(),
                HumiditySection(),
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TitleSection(
            currentTitleBoxHeight,
            location = uiState.location.name,
            currentTemp = uiState.weather.currentInfo.temperature,
            maxTemp = uiState.weather.forecastInfo.today.maxTemperature,
            minTemp = uiState.weather.forecastInfo.today.minTemperature,
            conditionCode = uiState.weather.forecastInfo.today.conditionCode,
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
                        -> DailyForecastSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is AqiSection,
                        -> AqiSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is SunriseSection,
                        -> SunriseSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is UVIndexSection,
                        -> UvIndexSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)

                    is WindSection,
                        -> WindSection(headerSectionHeight, section, measureContainerHeight, uiState.weather)


                    is HumiditySection,
                        -> StubSection(headerSectionHeight, section, measureContainerHeight)
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
private fun StubSection(
    headerSectionHeight: Dp,
    section: WeatherSection,
    measureContainerHeight: (Int) -> Unit,
) {
    CollapsableContainer(
        headerHeight = headerSectionHeight,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight240)
        )
    }
}

@Composable
private fun HourlyForecastSection(
    headerSectionHeight: Dp,
    section: HourlyForecastSection,
    weather: ForecastWeatherDomainModel,
    progress: Float,
    measureContainerHeight: (Int) -> Unit,
) {
    CollapsableContainerWithAnimatedHeader(
        minHeaderHeight = headerSectionHeight,
        currentBodyHeight = section.bodyHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        alertTitle = weather.alertsInfo.alerts.firstOrNull(),
        progress = progress,
        onContentMeasured = measureContainerHeight
    ) {
        ForecastHourlyLazyRow(
            modifier = Modifier.padding(horizontal = padding16),
            items = weather.forecastInfo.forecastHours
        )
    }
}

@Composable
private fun DailyForecastSection(
    headerSectionHeight: Dp,
    section: DailyForecastSection,
    measureContainerHeight: (Int) -> Unit,
    weather: ForecastWeatherDomainModel,
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
            items = weather.forecastInfo.forecastDays,
            currentTemperature = weather.currentInfo.temperature.toInt()
        )
    }
}

@Composable
private fun AqiSection(
    headerSectionHeight: Dp,
    section: WeatherSection,
    measureContainerHeight: (Int) -> Unit,
    weather: ForecastWeatherDomainModel,
) {
    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        AirQualityItem(
            modifier = Modifier.padding(horizontal = padding16),
            aqiIndex = weather.currentInfo.airQualityIndex.aqiIndex,
            coLevel = weather.currentInfo.airQualityIndex.coLevel,
            no2Level = weather.currentInfo.airQualityIndex.no2Level,
            o3Level = weather.currentInfo.airQualityIndex.o3Level,
            so2Level = weather.currentInfo.airQualityIndex.so2Level,
            pm25Level = weather.currentInfo.airQualityIndex.pm25Level,
            pm10Level = weather.currentInfo.airQualityIndex.pm10Level,
        )
    }
}

@Composable
private fun SunriseSection(
    headerSectionHeight: Dp,
    section: SunriseSection,
    measureContainerHeight: (Int) -> Unit,
    weather: ForecastWeatherDomainModel,
) {
    val today = weather.forecastInfo.today
    val location = weather.locationInfo

    val sunrise = today.sunrise
    val sunset = today.sunset
    val currentTime = location.locationTime
    val isMidDay = location.isMidDay

    if (sunrise != null && sunset != null && currentTime != null && isMidDay != null) {
        CollapsableContainer(
            headerHeight = headerSectionHeight,
            headerTitle = stringResource(section.title),
            headerIcon = section.icon,
            currentBodyHeight = section.bodyHeight,
            onContentMeasured = measureContainerHeight
        ) {
            SunriseItem(
                modifier = Modifier.padding(horizontal = padding16),
                sunriseTime = sunrise,
                sunsetTime = sunset,
                currentTime = currentTime,
                isMidDay = isMidDay
            )
        }
    }
}

@Composable
private fun UvIndexSection(
    headerSectionHeight: Dp,
    section: UVIndexSection,
    measureContainerHeight: (Int) -> Unit,
    weather: ForecastWeatherDomainModel,
) {
    val today = weather.currentInfo

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        UvIndexItem(
            modifier = Modifier.padding(horizontal = padding16),
            index = today.uvIndex.value
        )
    }
}

@Composable
private fun WindSection(
    headerSectionHeight: Dp,
    section: WindSection,
    measureContainerHeight: (Int) -> Unit,
    weather: ForecastWeatherDomainModel,
) {
    val today = weather.currentInfo

    CollapsableContainer(
        headerHeight = headerSectionHeight,
        headerTitle = stringResource(section.title),
        headerIcon = section.icon,
        currentBodyHeight = section.bodyHeight,
        onContentMeasured = measureContainerHeight
    ) {
        WindItem(
            modifier = Modifier.padding(horizontal = padding16),
            windSpeed = today.windSpeed,
            gustSpeed = today.gustSpeed,
            degree = today.windDegree,
            direction = today.windDirection
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
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
                ), weather = null
            ),
        )
    }
}