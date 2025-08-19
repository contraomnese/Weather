package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space32
import kotlinx.coroutines.flow.map
import kotlin.math.abs

@Composable
private fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun NestedScrollExample() {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    // title
    val minTitleBoxHeightPx = 60.dp.toPx()
    val maxTitleBoxHeightPx = 200.dp.toPx()
    var currentTitleBoxHeight by remember { mutableFloatStateOf(maxTitleBoxHeightPx) }

    // containers
    val verticalSpacing = 12.dp
    val collapsableContainerHeaderHeightDefault = 40.dp
    val collapsableContainerBodyHeightFirstPx = 200.dp.toPx()
    val collapsableContainerBodyHeightSecondPx = 700.dp.toPx()
    val collapsableContainerBodyHeightThirdPx = 400.dp.toPx()
    val collapsableContainerBodyHeightFourthPx = 200.dp.toPx()
    val collapsableContainerBodyHeightFifthPx = 200.dp.toPx()
    val collapsableContainerBodyHeightSixthPx = 200.dp.toPx()
    val collapsableContainerBodyHeightSeventhPx = 200.dp.toPx()

    val maxVisibleCollapsableContainerOffset =
        collapsableContainerHeaderHeightDefault.toPx() + verticalSpacing.toPx()

    var currentVisibleCollapseContainerIndex by remember { mutableIntStateOf(-1) }
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .map { vis -> vis.firstOrNull { it.offset >= -maxVisibleCollapsableContainerOffset }?.index ?: -1 }
            .collect { currentVisibleCollapseContainerIndex = it }
    }

    var collapsableContainersHeights by remember {
        mutableStateOf(
            listOf(
                collapsableContainerBodyHeightFirstPx to collapsableContainerBodyHeightFirstPx,
                collapsableContainerBodyHeightSecondPx to collapsableContainerBodyHeightSecondPx,
                collapsableContainerBodyHeightThirdPx to collapsableContainerBodyHeightThirdPx,
                collapsableContainerBodyHeightFourthPx to collapsableContainerBodyHeightFourthPx,
                collapsableContainerBodyHeightFifthPx to collapsableContainerBodyHeightFifthPx,
                collapsableContainerBodyHeightSixthPx to collapsableContainerBodyHeightSixthPx,
                collapsableContainerBodyHeightSeventhPx to collapsableContainerBodyHeightSeventhPx,
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
                        lastItem.index == collapsableContainersHeights.size &&
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
                if (availableScrollResult < 0 && currentVisibleCollapseContainerIndex == 0) {
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
                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentVisibleCollapseContainerIndex }

                val currentCollapseContainerOffset =
                    currentCollapseContainer?.offset?.toFloat() ?: Float.NEGATIVE_INFINITY

                // make sure we don't steal more scroll than needed to show a new container when scrolling DOWN
                if (availableScrollResult > 0f && currentCollapseContainerOffset < 0) {
                    if (availableScrollResult + currentCollapseContainerOffset > 0) {
                        val exactOffsetConsumed = availableScrollResult + currentCollapseContainerOffset
                        return Offset(0f, exactOffsetConsumed)
                    } else {
                        return Offset(0f, consumedResult)
                    }
                }

                // checking that all container's bodies after current container are expanded
                if (availableScrollResult > 0) {
                    collapsableContainersHeights.drop(currentVisibleCollapseContainerIndex + 1)
                        .forEach { (bodyContainerHeight, bodyContainerMaxHeight) ->
                            if (bodyContainerHeight != bodyContainerMaxHeight) {
                                val newBodyContainerHeight =
                                    (bodyContainerHeight + availableScrollResult).coerceIn(0f, bodyContainerMaxHeight)
                                collapsableContainersHeights = collapsableContainersHeights.mapIndexed { index, pair ->
                                    if (index == currentVisibleCollapseContainerIndex + 1) newBodyContainerHeight to bodyContainerMaxHeight else pair
                                }
                                val consumedScrollYByBody = newBodyContainerHeight - bodyContainerHeight
                                consumedResult += consumedScrollYByBody
                                availableScrollResult -= consumedScrollYByBody

                                if (newBodyContainerHeight == bodyContainerMaxHeight && availableScrollResult > maxVisibleCollapsableContainerOffset) {
                                    return Offset(0f, availableScrollResult - maxVisibleCollapsableContainerOffset)
                                }

                                return@forEach
                            }
                        }
                }

                val nextCollapseContainer =
                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentVisibleCollapseContainerIndex + 1 }

                val nextCollapseContainerOffset = nextCollapseContainer?.offset?.toFloat() ?: Float.POSITIVE_INFINITY

                // checking that current container's body is expanded
                val (currentBodyHeight, maxBodyHeight) = collapsableContainersHeights[currentVisibleCollapseContainerIndex]
                val newBodyHeight = (currentBodyHeight + availableScrollResult).coerceIn(0f, maxBodyHeight)
                collapsableContainersHeights = collapsableContainersHeights.mapIndexed { index, pair ->
                    if (index == currentVisibleCollapseContainerIndex) newBodyHeight to maxBodyHeight else pair
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
                if (newBodyHeight == maxBodyHeight && (currentCollapseContainerOffset + availableScrollResult) > maxVisibleCollapsableContainerOffset) {
                    val exactOffsetConsumed =
                        availableScrollResult + consumedResult - maxVisibleCollapsableContainerOffset

                    // for some reason when offset = 0 the next index does not take the correct value
                    currentVisibleCollapseContainerIndex--

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

    Column {
        // Фиксированный верхний хедер
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(density) { currentTitleBoxHeight.toDp() })
                .padding(horizontal = padding8)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = .0f)),
            contentAlignment = Alignment.Center
        ) {
            if (maxHeight > 120.dp) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(padding8)
                ) {
                    Text(
                        "123", style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "321", style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(padding8)
                ) {
                    Text(
                        "123", style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "321", style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding8, vertical = padding8)
                .nestedScroll(nestedScrollConnection),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        ) {
            // Первый контейнер
            items(collapsableContainersHeights) {
                ExpandableItem(
                    collapsableContainerHeaderHeightDefault,
                    it.first,
                    it.second
                ) {
                    repeat(10) { Text("Body$it") }
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
private fun ExpandableItem(
    headerHeight: Dp,
    currentHeight: Float,
    maxHeight: Float,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius16))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Header")
        }
        // Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, currentHeight.toInt()) {
                        val y = (maxHeight - currentHeight).toInt()
                        placeable.place(0, -y)
                    }
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(padding8),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CollapsingPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        NestedScrollExample()
    }
}
