package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.core.ui.canvas.WaterRainDropIcon
import com.contraomnese.weather.design.theme.WeatherTheme


@Composable
internal fun RainIcon(
    modifier: Modifier = Modifier,
    intensity: Intensity,
    precipitationProbability: Int? = null,
    color: Color = Color.White,
) {

    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        val rainDropSize = side * 0.15f
        CloudIcon(modifier = Modifier.fillMaxWidth(), color)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .offset(
                    y = side * .58f
                ),
            verticalArrangement = Arrangement.spacedBy(side * .01f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (intensity) {
                Intensity.LIGHT -> LightIntensityRainDropLine(rainDropSize)
                Intensity.MODERATE -> ModerateIntensityRainDropLine(rainDropSize)
                Intensity.HEAVY -> HeavyIntensityRainDropLine(rainDropSize)
            }

            precipitationProbability?.let {
                PrecipitationProbabilityIcon(
                    modifier = Modifier.fillMaxWidth(),
                    it,
                    side
                )
            }
        }
    }
}


@Composable
internal fun ColumnScope.LightIntensityRainDropLine(
    size: Dp,
) {
    WaterRainDropLineRange(
        count = 1,
        size = size
    )
}

@Composable
internal fun ColumnScope.ModerateIntensityRainDropLine(
    size: Dp,
) {
    WaterRainDropLineRange(
        count = 3,
        size = size
    )
}

@Composable
internal fun ColumnScope.HeavyIntensityRainDropLine(
    size: Dp,
) {
    WaterRainDropLineRange(
        count = 5,
        size = size
    )
}

@Composable
internal fun ColumnScope.WaterRainDropLineRange(
    modifier: Modifier = Modifier,
    count: Int = 6,
    size: Dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(count) {
            WaterRainDropIcon(
                modifier = Modifier
                    .size(size)
                    .align(Alignment.CenterVertically)
                    .graphicsLayer {
                        rotationZ = 15f
                    },
            )
        }
    }
}


@Preview
@Composable
fun RainIconPreview() {
    WeatherTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RainIcon(
                modifier = Modifier.size(128.dp),
                intensity = Intensity.LIGHT,
                precipitationProbability = null
            )
            RainIcon(
                modifier = Modifier
                    .size(128.dp)
                    .background(Color.Red),
                intensity = Intensity.MODERATE,
                precipitationProbability = 30
            )
            RainIcon(
                modifier = Modifier.size(128.dp),
                intensity = Intensity.HEAVY,
                precipitationProbability = 60
            )
        }
    }
}