package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.itemWidth112
import com.contraomnese.weather.design.theme.itemWidth16
import com.contraomnese.weather.design.theme.padding8

@Composable
fun Pointer(
    elements: List<Int>,
    currentIndex: Int,
) {
    val density = LocalDensity.current
    val lazyListState = rememberLazyListState()

    LaunchedEffect(currentIndex) {
        val centerOffset = (lazyListState.layoutInfo.viewportEndOffset -
                lazyListState.layoutInfo.viewportStartOffset) / 2

        lazyListState.animateScrollToItem(
            index = currentIndex,
            scrollOffset = (-centerOffset + with(density) { itemWidth16.toPx() / 2 }).toInt()
        )
    }

    LazyRow(
        modifier = Modifier.widthIn(max = itemWidth112),
        state = lazyListState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding8),
        userScrollEnabled = false
    ) {
        items(elements.size) { index ->
            val isSelected = currentIndex == index
            Icon(
                modifier = Modifier.size(itemWidth16),
                imageVector = if (isSelected) WeatherIcons.CircleFilled else WeatherIcons.Circle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}