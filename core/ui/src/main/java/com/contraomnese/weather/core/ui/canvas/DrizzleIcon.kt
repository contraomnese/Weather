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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding8

internal sealed class Intensity {
    data object LIGHT : Intensity()
    data object MODERATE : Intensity()
    data object HEAVY : Intensity()
}

@Composable
internal fun DrizzleIcon(
    modifier: Modifier = Modifier,
    intensity: Intensity,
) {
    BoxWithConstraints(modifier) {

        CloudIcon(
            modifier = Modifier
                .size(this.maxWidth)
                .align(Alignment.TopCenter),
            cloudColor = Color(0xFF97A2AC),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.27f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (intensity) {
                Intensity.LIGHT -> LightIntensityRainDropCircle()
                Intensity.MODERATE -> ModerateIntensityRainDropCircle()
                Intensity.HEAVY -> HeavyIntensityRainDropCircle()
            }
        }

    }
}


@Composable
internal fun ColumnScope.LightIntensityRainDropCircle() {
    repeat(5) { number ->
        RainDropCircleRange(
            Modifier
                .weight(1f)
                .padding(
                    start = padding8 * (1 - 0.2f * (number + 1)),
                    end = padding8 * (0.2f * (number + 1))
                ),
            colorAlpha = 0.1f * (number + 1),
            count = 3,
            size = 3f,
        )
    }
}

@Composable
internal fun ColumnScope.ModerateIntensityRainDropCircle() {
    repeat(5) { number ->
        RainDropCircleRange(
            Modifier
                .weight(1f)
                .padding(
                    start = padding8 * (1 - 0.2f * (number + 1)),
                    end = padding8 * (0.2f * (number + 1))
                ),
            colorAlpha = 0.1f * (number + 1),
            count = 5,
            size = 3f,
        )
    }
}

@Composable
internal fun ColumnScope.HeavyIntensityRainDropCircle() {
    repeat(5) { number ->
        RainDropCircleRange(
            Modifier
                .weight(1f)
                .padding(
                    start = padding8 * (1 - 0.2f * (number + 1)),
                    end = padding8 * (0.2f * (number + 1))
                ),
            colorAlpha = 0.1f * (number + 1),
            count = 7,
            size = 3f,
        )
    }
}

@Composable
internal fun ColumnScope.RainDropCircleRange(
    modifier: Modifier = Modifier,
    count: Int = 6,
    size: Float = 2f,
    colorAlpha: Float = 0.5f,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .alpha(colorAlpha.coerceAtMost(0.5f))
    ) {
        repeat(count) {
            RainDropCircleIcon(
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
fun DrizzleIconPreview() {
    WeatherTheme {
        Column {
            DrizzleIcon(modifier = Modifier.size(200.dp), intensity = Intensity.LIGHT)
            DrizzleIcon(modifier = Modifier.size(200.dp), intensity = Intensity.MODERATE)
            DrizzleIcon(modifier = Modifier.size(200.dp), intensity = Intensity.HEAVY)
        }
    }
}