package com.contraomnese.weather.weatherByLocation.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.contraomnese.weather.core.ui.widgets.TitleSection
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space32
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
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
internal fun WeatherScreen(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
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
    val dailySection = DailyForecastSection(250.dp.toPx())
    val hourlySection = HourlyForecastSection(180.dp.toPx())
    val aqiSection = AqiSection(180.dp.toPx())
    val uvIndexSection = UVIndexSection(200.dp.toPx())
    val humiditySection = HumiditySection(200.dp.toPx())
    val sunriseSection = SunriseSection(400.dp.toPx())
    val windSection = WindSection(400.dp.toPx())

    val maxVisibleSectionOffset = headerSectionHeight.toPx() + sectionVerticalSpacing.toPx()
    var currentVisibleSectionIndex by remember { mutableIntStateOf(0) }

    var sections by remember {
        mutableStateOf(
            listOf(
                hourlySection,
                dailySection,
                aqiSection,
                uvIndexSection,
                humiditySection,
                sunriseSection,
                windSection
            )
        )
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                Log.d("123", "$sections")
                Log.d(
                    "123",
                    "lazyListState.layoutInfo.visibleItemsInfo ${lazyListState.layoutInfo.visibleItemsInfo.map { it.index }}"
                )
                Log.d("123", "currentVisibleSectionIndex - $currentVisibleSectionIndex")
                Log.d("123", "LOG1")
                if (available.y == 0f) return Offset.Zero

                // disable scroll when last item is visible
                val lastItem = lazyListState.layoutInfo.let {
                    it.visibleItemsInfo.firstOrNull { lastItem ->
                        lastItem.index == sections.size &&
                                lastItem.offset + lastItem.size > it.viewportEndOffset
                    }
                }
                if (lastItem != null && available.y < 0) {
                    Log.d("123", "LOG2")
                    return Offset(0f, available.y)
                }
                // end block

                var consumedResult = 0f
                var availableScrollResult = available.y

                // steal scroll to change title height
                if (availableScrollResult < 0 && currentVisibleSectionIndex == 0) {
                    Log.d("123", "LOG3")
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
                    Log.d("123", "LOG4")
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
                                val newHeight = (bodyHeight + availableScrollResult).coerceIn(0f, maxHeight)

                                sections = sections.mapIndexed { idx, sec ->
                                    val targetIdx = nextIndex + currentVisibleSectionIndex + 1
                                    if (idx == targetIdx) {
                                        sec.copyWithBodyHeight(newHeight)
                                    } else sec
                                }
                                Log.d("123", "LOG5")
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
                    Log.d("123", "LOG6")
                    return Offset(0f, consumedResult)
                }
                // end block

                Log.d("123", "currentCollapseContainerOffset: $currentCollapseContainerOffset")
                // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                if (availableScrollResult > 0f && currentCollapseContainerOffset < 0) {
                    Log.d("123", "availableScrollResult: $availableScrollResult")
                    return if (availableScrollResult + currentCollapseContainerOffset > 0) {
                        Log.d("123", "LOG7")
                        val exactOffsetConsumed = availableScrollResult + currentCollapseContainerOffset
                        if (currentVisibleSectionIndex > 0) currentVisibleSectionIndex--
                        Offset(0f, exactOffsetConsumed)
                    } else {
                        Log.d("123", "LOG8")
                        Offset(0f, consumedResult)
                    }
                }

                val nextCollapseContainer =
                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentVisibleSectionIndex + 1 }

                val nextCollapseContainerOffset = nextCollapseContainer?.offset?.toFloat() ?: Float.POSITIVE_INFINITY

                // checking that current container's body is expanded
                val (currentBodyHeight, maxBodyHeight) = sections[currentVisibleSectionIndex]
                val newBodyHeight = (currentBodyHeight + availableScrollResult).coerceIn(0f, maxBodyHeight)
                sections = sections.mapIndexed { idx, type ->
                    if (idx == currentVisibleSectionIndex) {
                        type.copyWithBodyHeight(newBodyHeight)
                    } else type
                }
                val consumedScrollYByBody = newBodyHeight - currentBodyHeight
                consumedResult += consumedScrollYByBody
                availableScrollResult -= consumedScrollYByBody

                // make sure we don't steal more scroll than needed to show a new container when scrolling UP
                if (newBodyHeight == 0f && availableScrollResult < 0 && abs(availableScrollResult + consumedResult) > nextCollapseContainerOffset) {
                    Log.d("123", "LOG9")
                    Log.d("123", "availableScrollResult: $availableScrollResult")
                    Log.d("123", "consumedResult: $consumedResult")
                    Log.d("123", "nextCollapseContainerOffset: $nextCollapseContainerOffset")
                    val exactOffsetConsumed = consumedResult + availableScrollResult + nextCollapseContainerOffset
                    Log.d("123", "exactOffsetConsumed: $exactOffsetConsumed")
                    if (currentVisibleSectionIndex < sections.size - 1) currentVisibleSectionIndex++
                    return Offset(0f, exactOffsetConsumed)
                }
                Log.d("123", "maxVisibleSectionOffset: $maxVisibleSectionOffset")
                // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                if (newBodyHeight == maxBodyHeight && (currentCollapseContainerOffset + availableScrollResult) > maxVisibleSectionOffset) {
                    val exactOffsetConsumed =
                        currentCollapseContainerOffset + availableScrollResult + consumedResult - maxVisibleSectionOffset
                    // for some reason when offset = 0 the next index does not take the correct value
                    Log.d("123", "availableScrollResult: $availableScrollResult")
                    Log.d("123", "consumedResult: $consumedResult")
                    Log.d("123", "exactOffsetConsumed: $exactOffsetConsumed")
                    Log.d(
                        "123",
                        "currentCollapseContainerOffset + availableScrollResult: ${currentCollapseContainerOffset + availableScrollResult}"
                    )
                    Log.d("123", "LOG10")
                    if (currentVisibleSectionIndex > 0) currentVisibleSectionIndex--
                    return Offset(0f, exactOffsetConsumed)
                }
                // we give away the remaining scroll
                Log.d("123", "LOG11")
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

//    LaunchedEffect(lazyListState) {
//        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
//            .map { vis -> vis.firstOrNull { it.offset >= -maxVisibleSectionOffset }?.index ?: -1 }
//            .collect { currentVisibleSectionIndex = it }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                when (section) {
                    is HourlyForecastSection,
                            -> CollapsableContainerWithAnimatedHeader(
                            minHeaderHeight = headerSectionHeight,
                        currentBodyHeight = section.bodyHeight,
                        maxBodyHeight = section.bodyMaxHeight,
                            headerTitle = stringResource(R.string.today_forecast_title),
                            headerIcon = WeatherIcons.Today,
                            alertTitle = uiState.weather.alertsInfo.alerts.firstOrNull(),
                            progress = progress
                        ) {
                            ForecastHourlyLazyRow(
                                modifier = Modifier.padding(horizontal = padding16),
                                items = uiState.weather.forecastInfo.forecastHours
                            )
                        }

                    is DailyForecastSection,
                            -> CollapsableContainer(
                            headerHeight = headerSectionHeight,
                            headerTitle = stringResource(R.string.daily_forecast_title),
                            headerIcon = WeatherIcons.Daily,
                        currentBodyHeight = section.bodyHeight,
                        maxBodyHeight = section.bodyMaxHeight,
                        ) {
                            ForecastDailyColumn(
                                modifier = Modifier.padding(horizontal = padding16),
                                items = uiState.weather.forecastInfo.forecastDays,
                                currentTemperature = uiState.weather.currentInfo.temperature.toInt()
                            )
                        }

                    is AqiSection,
                            -> CollapsableContainer(
                            headerHeight = headerSectionHeight,
                            headerTitle = stringResource(R.string.aqi_title),
                            headerIcon = WeatherIcons.Aqi,
                        currentBodyHeight = section.bodyHeight,
                        maxBodyHeight = section.bodyMaxHeight,
                        ) {
                            AirQualityItem(
                                modifier = Modifier.padding(horizontal = padding16),
                                aqiIndex = uiState.weather.currentInfo.airQualityIndex.aqiIndex,
                                coLevel = uiState.weather.currentInfo.airQualityIndex.coLevel,
                                no2Level = uiState.weather.currentInfo.airQualityIndex.no2Level,
                                o3Level = uiState.weather.currentInfo.airQualityIndex.o3Level,
                                so2Level = uiState.weather.currentInfo.airQualityIndex.so2Level,
                                pm25Level = uiState.weather.currentInfo.airQualityIndex.pm25Level,
                                pm10Level = uiState.weather.currentInfo.airQualityIndex.pm10Level,
                            )
                        }

                    is SunriseSection, is UVIndexSection, is HumiditySection, is WindSection,
                        -> CollapsableContainer(
                        headerHeight = headerSectionHeight,
                        currentBodyHeight = section.bodyHeight,
                        maxBodyHeight = section.bodyMaxHeight,
                    ) {
                        repeat(5) { Text("Body$it") }
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
private fun WeatherSectionItem(
    section: WeatherSection,
    uiState: WeatherUiState,
    headerHeight: Dp,
    padding: Dp,
    progress: Float,
) {
    requireNotNull(uiState.weather)

    Section(section, uiState, headerHeight, padding, progress)

}

@Composable
private fun Section(
    section: WeatherSection,
    uiState: WeatherUiState,
    headerHeight: Dp,
    padding: Dp,
    progress: Float,
) {
    requireNotNull(uiState.weather)

    when (section) {
        is HourlyForecastSection -> CollapsableContainerWithAnimatedHeader(
            minHeaderHeight = headerHeight,
            currentBodyHeight = section.bodyHeight,
            maxBodyHeight = section.bodyMaxHeight,
            headerTitle = stringResource(R.string.today_forecast_title),
            headerIcon = WeatherIcons.Today,
            alertTitle = uiState.weather.alertsInfo.alerts.firstOrNull(),
            progress = progress
        ) {
            ForecastHourlyLazyRow(
                modifier = Modifier.padding(horizontal = padding),
                items = uiState.weather.forecastInfo.forecastHours
            )
        }

        is DailyForecastSection -> CollapsableContainer(
            headerHeight = headerHeight,
            headerTitle = stringResource(R.string.daily_forecast_title),
            headerIcon = WeatherIcons.Daily,
            currentBodyHeight = section.bodyHeight,
            maxBodyHeight = section.bodyMaxHeight,
        ) {
            ForecastDailyColumn(
                modifier = Modifier.padding(horizontal = padding),
                items = uiState.weather.forecastInfo.forecastDays,
                currentTemperature = uiState.weather.currentInfo.temperature.toInt()
            )
        }

        is AqiSection -> CollapsableContainer(
            headerHeight = headerHeight,
            headerTitle = stringResource(R.string.aqi_title),
            headerIcon = WeatherIcons.Aqi,
            currentBodyHeight = section.bodyHeight,
            maxBodyHeight = section.bodyMaxHeight,
        ) {
            AirQualityItem(
                modifier = Modifier.padding(horizontal = padding),
                aqiIndex = uiState.weather.currentInfo.airQualityIndex.aqiIndex,
                coLevel = uiState.weather.currentInfo.airQualityIndex.coLevel,
                no2Level = uiState.weather.currentInfo.airQualityIndex.no2Level,
                o3Level = uiState.weather.currentInfo.airQualityIndex.o3Level,
                so2Level = uiState.weather.currentInfo.airQualityIndex.so2Level,
                pm25Level = uiState.weather.currentInfo.airQualityIndex.pm25Level,
                pm10Level = uiState.weather.currentInfo.airQualityIndex.pm10Level,
            )
        }

        is SunriseSection, is WindSection, is HumiditySection, is UVIndexSection,
            -> CollapsableContainer(
            headerHeight = headerHeight,
            currentBodyHeight = section.bodyHeight,
            maxBodyHeight = section.bodyMaxHeight,
        ) {
            repeat(5) { Text("Body$it") }
        }
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