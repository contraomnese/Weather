package com.contraomnese.weather.core.ui.icons

import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.core.ui.canvas.SnowFlakeIcon
import com.contraomnese.weather.design.theme.WeatherTheme

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun SnowCloudIcon(
    modifier: Modifier = Modifier,
    intensity: Intensity,
    precipitationProbability: Int? = null,
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        val snowFlakeSize = side * 0.15f
        CloudIcon(modifier = Modifier.fillMaxWidth())

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
                Intensity.LIGHT -> LightIntensitySnowFlakeLine(snowFlakeSize)
                Intensity.MODERATE -> ModerateIntensitySnowFlakeLine(snowFlakeSize)
                Intensity.HEAVY -> HeavyIntensitySnowFlakeLine(snowFlakeSize)
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
internal fun ColumnScope.LightIntensitySnowFlakeLine(snowFlakeSize: Dp) {
    SnowFlakeRange(
        count = 1,
        snowFlakeSize = snowFlakeSize
    )
}

@Composable
internal fun ColumnScope.ModerateIntensitySnowFlakeLine(snowFlakeSize: Dp) {
    SnowFlakeRange(
        count = 3,
        snowFlakeSize = snowFlakeSize
    )
}

@Composable
internal fun ColumnScope.HeavyIntensitySnowFlakeLine(snowFlakeSize: Dp) {
    SnowFlakeRange(
        count = 5,
        snowFlakeSize = snowFlakeSize
    )
}

@Composable
internal fun ColumnScope.SnowFlakeRange(
    count: Int = 4,
    snowFlakeSize: Dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(count) {
            SnowFlakeIcon(
                modifier = Modifier
                    .size(snowFlakeSize)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun SnowCloudIconPreview() {
    WeatherTheme {
        Column {
            SnowCloudIcon(
                modifier = Modifier.size(64.dp),
                intensity = Intensity.LIGHT,
                precipitationProbability = 10
            )
            SnowCloudIcon(
                modifier = Modifier.size(64.dp),
                intensity = Intensity.MODERATE,
                precipitationProbability = 40
            )
            SnowCloudIcon(
                modifier = Modifier.size(64.dp),
                intensity = Intensity.HEAVY,
                precipitationProbability = 70
            )
        }

    }
}