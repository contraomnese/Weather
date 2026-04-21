package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.ThunderIcon
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
internal fun ThunderCloudWithRainIcon(
    modifier: Modifier = Modifier,
    intensity: Intensity,
    precipitationProbability: Int? = null,
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        RainIcon(
            modifier = Modifier
                .fillMaxSize(),
            intensity = intensity,
            precipitationProbability = precipitationProbability,
            color = Color(0xFF97A2AC)
        )

        ThunderIcon(
            modifier = Modifier
                .size(side * .4f)
                .offset(
                    y = side * 0.25f
                ),
            thunderColor = Color(0xFFF1C312)
        )
    }
}

@Preview
@Composable
fun ThunderCloudWithRainIconPreview() {
    WeatherTheme {
        Column {
            ThunderCloudWithRainIcon(
                modifier = Modifier
                    .size(128.dp),
                intensity = Intensity.MODERATE,
                precipitationProbability = 10
            )
            ThunderCloudWithRainIcon(
                modifier = Modifier
                    .size(128.dp),
                intensity = Intensity.HEAVY,
                precipitationProbability = 50
            )
        }
    }
}