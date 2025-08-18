package com.contraomnese.weather.weatherByLocation.presentation

import android.util.Log
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
import com.contraomnese.weather.design.theme.space16
import kotlinx.coroutines.flow.map
import kotlin.math.abs

@Composable
private fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun NestedScrollExample() {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val headerHeightDefault = 40.dp
    val verticalSpacing = 12.dp
    val bodyMaxHeightFirstPx = 200.dp.toPx()
    val bodyMaxHeightSecondPx = 700.dp.toPx()
    val bodyMaxHeightThirdPx = 400.dp.toPx()
    val bodyMaxHeightFourthPx = 200.dp.toPx()
    val bodyMaxHeightFifthPx = 200.dp.toPx()
    val bodyMaxHeightSixthPx = 200.dp.toPx()
    val bodyMaxHeightSeventhPx = 200.dp.toPx()

    var currentBodyIndexState by remember { mutableIntStateOf(-1) }

    val minColumnHeaderHeightPx = 60.dp.toPx()
    val maxColumnHeaderHeightPx = 200.dp.toPx()
    var currentColumnHeaderHeight by remember { mutableFloatStateOf(maxColumnHeaderHeightPx) }

    val maxItemOffset = headerHeightDefault.toPx() + verticalSpacing.toPx()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .map { vis -> vis.firstOrNull { it.offset >= -maxItemOffset }?.index ?: -1 }
            .collect { currentBodyIndexState = it }
    }

    var currentBodyHeights by remember {
        mutableStateOf(
            listOf(
                bodyMaxHeightFirstPx to bodyMaxHeightFirstPx,
                bodyMaxHeightSecondPx to bodyMaxHeightSecondPx,
                bodyMaxHeightThirdPx to bodyMaxHeightThirdPx,
                bodyMaxHeightFourthPx to bodyMaxHeightFourthPx,
                bodyMaxHeightFifthPx to bodyMaxHeightFifthPx,
                bodyMaxHeightSixthPx to bodyMaxHeightSixthPx,
                bodyMaxHeightSeventhPx to bodyMaxHeightSeventhPx,
            )
        )
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                Log.d("123", "INDEX: $currentBodyIndexState")

                if (available.y == 0f) return Offset.Zero

                var consumedScrollY = 0f
                var reserveScrollY = available.y
                Log.d("123", "reserveScrollY: ($reserveScrollY)")
                // ==== 1) header растет/сжимается ====
                if (available.y < 0 && currentBodyIndexState == 0 && lazyListState.firstVisibleItemScrollOffset == 0) {
                    val prevHeaderHeight = currentColumnHeaderHeight
                    currentColumnHeaderHeight =
                        (currentColumnHeaderHeight + reserveScrollY).coerceIn(
                            minColumnHeaderHeightPx,
                            maxColumnHeaderHeightPx
                        )
                    val headerConsumed = currentColumnHeaderHeight - prevHeaderHeight
                    consumedScrollY += headerConsumed
                    reserveScrollY -= headerConsumed

                    Log.d("123", "HEADER ЗАБРАЛ $headerConsumed PX")
                    Log.d("123", "ОСТАЛОСЬ $reserveScrollY PX")
                }

                if (reserveScrollY == 0f) return Offset(0f, consumedScrollY)

                // ==== 2) body текущего элемента ====
                val bodyIndex = currentBodyIndexState
                Log.d("123", "bodyIndex $bodyIndex")
                val currentCollapseContainer =
                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == bodyIndex }
                val currentCollapseContainerOffset =
                    currentCollapseContainer?.offset?.toFloat() ?: Float.NEGATIVE_INFINITY

                Log.d("123", "ОТСТУП ТЕКУЩЕГО КОНТЕЙНЕРА $currentCollapseContainerOffset PX")

                val nextCollapseContainer =
                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == bodyIndex + 1 }
                val nextCollapseContainerOffset = nextCollapseContainer?.offset?.toFloat() ?: Float.POSITIVE_INFINITY
                Log.d("123", "ОТСТУП СЛЕДУЮЩЕГО КОНТЕЙНЕРА $nextCollapseContainerOffset PX")

                if (bodyIndex in currentBodyHeights.indices) {

                    val (nextBodyHeight, nextMaxBodyHeight) = currentBodyHeights[bodyIndex + 1]
                    if (available.y > 0 && nextBodyHeight != nextMaxBodyHeight) {
                        val newBodyHeight = (nextBodyHeight + reserveScrollY).coerceIn(0f, nextMaxBodyHeight)
                        currentBodyHeights = currentBodyHeights.mapIndexed { index, pair ->
                            if (index == bodyIndex + 1) newBodyHeight to nextMaxBodyHeight else pair
                        }
                        val consumedScrollYByBody = newBodyHeight - nextBodyHeight
                        consumedScrollY += consumedScrollYByBody
                        reserveScrollY -= consumedScrollYByBody

                        Log.d("123", "BODY ЗАБРАЛ $consumedScrollYByBody PX")
                        Log.d("123", "ОСТАЛОСЬ $reserveScrollY PX")

                        if (newBodyHeight == 0f && reserveScrollY < 0 && abs(reserveScrollY + consumedScrollY) > nextCollapseContainerOffset) {
                            val exactOffsetConsumed = reserveScrollY + consumedScrollY + nextCollapseContainerOffset
                            Log.d("123", "EXACT OFFSET: $exactOffsetConsumed")
                            return Offset(0f, exactOffsetConsumed)
                        }
                        if (newBodyHeight == nextMaxBodyHeight && reserveScrollY > maxItemOffset) {
                            return Offset(0f, reserveScrollY - maxItemOffset)
                        }
                    }

                    if (reserveScrollY > 0f && currentCollapseContainerOffset < 0) {
                        if (reserveScrollY + nextCollapseContainerOffset > maxItemOffset) {
                            val exactOffsetConsumed = reserveScrollY - (maxItemOffset - nextCollapseContainerOffset)
                            return Offset(0f, exactOffsetConsumed)
                        } else return Offset(0f, consumedScrollY)
                    }

                    val (currentBodyHeight, maxBodyHeight) = currentBodyHeights[bodyIndex]
                    val newBodyHeight = (currentBodyHeight + reserveScrollY).coerceIn(0f, maxBodyHeight)
                    currentBodyHeights = currentBodyHeights.mapIndexed { index, pair ->
                        if (index == bodyIndex) newBodyHeight to maxBodyHeight else pair
                    }
                    val consumedScrollYByBody = newBodyHeight - currentBodyHeight
                    consumedScrollY += consumedScrollYByBody
                    reserveScrollY -= consumedScrollYByBody

                    Log.d("123", "BODY ЗАБРАЛ $consumedScrollYByBody PX")
                    Log.d("123", "ОСТАЛОСЬ $reserveScrollY PX")

                    if (newBodyHeight == 0f && reserveScrollY < 0 && abs(reserveScrollY + consumedScrollY) > nextCollapseContainerOffset) {
                        val exactOffsetConsumed = reserveScrollY + consumedScrollY + nextCollapseContainerOffset
                        Log.d("123", "EXACT OFFSET: $exactOffsetConsumed")
                        return Offset(0f, exactOffsetConsumed)
                    }
                    if (newBodyHeight == maxBodyHeight && reserveScrollY > maxItemOffset) {
                        return Offset(0f, reserveScrollY - maxItemOffset)

                    }
                }

                Log.d("123", "ДВИГАЕМ СПИСОК НА ($reserveScrollY) PX")
                return Offset(0f, consumedScrollY)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val dy = available.y
                if (dy > 0 && lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0) {
                    val prev = currentColumnHeaderHeight
                    currentColumnHeaderHeight =
                        (currentColumnHeaderHeight + dy).coerceIn(minColumnHeaderHeightPx, maxColumnHeaderHeightPx)
                    return Offset(0f, currentColumnHeaderHeight - prev)
                }
                return Offset.Zero
            }
        }
    }

    Column {
        // Фиксированный верхний хедер
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(density) { currentColumnHeaderHeight.toDp() })
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
            items(currentBodyHeights) {
                ExpandableItem(
                    headerHeightDefault,
                    it.first,
                    it.second
                ) {
                    repeat(10) { Text("Body$it") }
                }
            }
            item {
                Spacer(modifier = Modifier.height(space16))
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
