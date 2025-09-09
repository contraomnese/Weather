package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.Pressure
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight180
import kotlinx.collections.immutable.persistentMapOf

private sealed interface PressureFractions {
    data object Normal : PressureFractions
    data object Moderate : PressureFractions
    data object High : PressureFractions
    data object Low : PressureFractions

    companion object {
        fun from(value: Int): PressureFractions {
            return when (value) {
                in 755..759 -> Moderate
                in 740..754 -> Low
                in 762..780 -> High
                else -> Normal
            }
        }
    }
}

private val pressureDescriptions = persistentMapOf(
    PressureFractions.Normal to R.string.pressure_is_good,
    PressureFractions.Moderate to R.string.pressure_is_moderate,
    PressureFractions.Low to R.string.pressure_is_low,
    PressureFractions.High to R.string.pressure_is_high,
)

@Composable
fun PressureItem(
    modifier: Modifier = Modifier,
    value: Int,
) {

    val fractionDescription = remember { pressureDescriptions.getValue(PressureFractions.from(value)) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight180),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
            text = stringResource(fractionDescription),
            style = MaterialTheme.typography.titleSmall.copy(
                lineBreak = LineBreak.Heading
            ),
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
        Pressure(
            modifier = Modifier.size(itemHeight180),
            pressure = value
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x79397E93)
@Composable
private fun PressureItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        PressureItem(
            modifier = modifier,
            value = 758
        )
    }
}