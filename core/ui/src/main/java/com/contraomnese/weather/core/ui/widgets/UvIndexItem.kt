package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.UvIndexRangeLine
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight8
import com.contraomnese.weather.design.theme.padding4

private data class UvIndex(
    @StringRes val title: Int,
    @StringRes val desc: Int,
)

private val availableUvIndex = mapOf(
    0..2 to UvIndex(title = R.string.low_title, desc = R.string.uv_index_low_desc),
    3..5 to UvIndex(title = R.string.moderate_title, desc = R.string.uv_index_moderate_desc),
    6..7 to UvIndex(title = R.string.high_title, desc = R.string.uv_index_high_desc),
    8..10 to UvIndex(title = R.string.very_high_title, desc = R.string.uv_index_very_high_desc),
    11..Int.MAX_VALUE to UvIndex(title = R.string.extreme_title, desc = R.string.uv_index_extreme_desc)
)

private fun handleUvIndex(index: Int): UvIndex = availableUvIndex.entries.find { (range, _) ->
    index in range
}?.value ?: availableUvIndex.values.last()

@Composable
fun UvIndexItem(
    modifier: Modifier = Modifier,
    index: Int,
) {

    val uvIndex by remember(index) { mutableStateOf(handleUvIndex(index)) }

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(padding4),
    ) {

        Text(
            text = index.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(uvIndex.title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        UvIndexRangeLine(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight8),
            current = index.toFloat(),
        )

        Text(
            text = stringResource(uvIndex.desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

}

@Preview(showBackground = true, backgroundColor = 0x79397E93)
@Composable
fun UvIndexItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        UvIndexItem(
            modifier = modifier,
            index = 2,
        )
    }
}