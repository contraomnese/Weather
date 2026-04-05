package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8


@Composable
internal fun RainIcon(
    modifier: Modifier = Modifier,
    intensity: Intensity,
) {
    BoxWithConstraints(modifier) {

        CloudIcon(
            modifier = Modifier
                .size(this.maxWidth)
                .align(Alignment.TopCenter),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = padding8)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.25f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (intensity) {
                Intensity.LIGHT -> LightIntensityRainDropLine()
                Intensity.MODERATE -> ModerateIntensityRainDropLine()
                Intensity.HEAVY -> HeavyIntensityRainDropLine()
            }
        }

    }
}


@Composable
internal fun ColumnScope.LightIntensityRainDropLine() {
    repeat(1) { number ->
        RainDropLineRange(
            Modifier
                .weight(1f)
                .padding(horizontal = padding4),
            count = 4,
            size = 4f
        )
    }
}

@Composable
internal fun ColumnScope.ModerateIntensityRainDropLine() {
    repeat(1) { number ->
        RainDropLineRange(
            Modifier.weight(1f),
            count = 6,
        )
    }
}

@Composable
internal fun ColumnScope.HeavyIntensityRainDropLine() {
    repeat(1) { number ->
        RainDropLineRange(
            Modifier.weight(1f),
            count = 8,
        )
    }
}

@Composable
internal fun ColumnScope.RainDropLineRange(
    modifier: Modifier = Modifier,
    count: Int = 6,
    size: Float = 3f,
) {
    Row(modifier = modifier.fillMaxSize()) {
        repeat(count) {
            RainDropLineIcon(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically),
                rainDropRatio = size
            )
        }
    }
}


@Preview
@Composable
fun RainIconPreview() {
    WeatherTheme {
        Column {
            RainIcon(modifier = Modifier.size(64.dp), intensity = Intensity.LIGHT)
            RainIcon(modifier = Modifier.size(64.dp), intensity = Intensity.MODERATE)
            RainIcon(modifier = Modifier.size(64.dp), intensity = Intensity.HEAVY)
        }
    }
}