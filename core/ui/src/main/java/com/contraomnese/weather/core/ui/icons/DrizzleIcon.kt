package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.core.ui.canvas.RainDropCircleIcon
import com.contraomnese.weather.design.theme.WeatherTheme

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
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        val drizzleSize = side * 0.05f
        CloudIcon(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF97A2AC),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .offset(
                    y = side * .6f
                ),
            horizontalAlignment = Alignment.Start
        ) {
            when (intensity) {
                Intensity.LIGHT -> LightIntensityRainDropCircle(drizzleSize)
                Intensity.MODERATE -> ModerateIntensityRainDropCircle(drizzleSize)
                Intensity.HEAVY -> HeavyIntensityRainDropCircle(drizzleSize)
            }
        }
    }
}


@Composable
internal fun ColumnScope.LightIntensityRainDropCircle(
    drizzleSize: Dp,
) {
    repeat(3) {
        RainDropCircleRange(
            count = 3,
            drizzleSize = drizzleSize,
            number = it
        )
    }
}

@Composable
internal fun ColumnScope.ModerateIntensityRainDropCircle(
    drizzleSize: Dp,
) {
    repeat(3) {
        RainDropCircleRange(
            count = 5,
            drizzleSize = drizzleSize,
            number = it,
        )
    }
}

@Composable
internal fun ColumnScope.HeavyIntensityRainDropCircle(
    drizzleSize: Dp,
) {
    repeat(3) {
        RainDropCircleRange(
            count = 7,
            drizzleSize = drizzleSize,
            number = it
        )
    }
}

@Composable
internal fun ColumnScope.RainDropCircleRange(
    modifier: Modifier = Modifier,
    count: Int = 6,
    drizzleSize: Dp,
    number: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.7f)
            .align(Alignment.Start)
            .height(drizzleSize * 2)

            .offset(
                x = drizzleSize * 3f * (1 - 0.2f * (number + 1))
            )
            .padding(start = drizzleSize * 2)
            .alpha((0.1f * (number + 1)).coerceAtMost(0.5f)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(count) {
            RainDropCircleIcon(
                modifier = Modifier
                    .size(drizzleSize)
                    .align(Alignment.CenterVertically),
            )
        }
    }
}


@Preview
@Composable
fun DrizzleIconPreview() {
    WeatherTheme {
        Column {
            DrizzleIcon(
                modifier = Modifier.width(128.dp),
                intensity = Intensity.LIGHT
            )
            DrizzleIcon(
                modifier = Modifier.width(128.dp),
                intensity = Intensity.HEAVY
            )
//            DrizzleIcon(
//                modifier = Modifier.width(64.dp).height(128.dp),
//                intensity = Intensity.HEAVY
//            )
        }
    }
}