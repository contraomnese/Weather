package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.ClearIcon
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.design.theme.WeatherTheme


@Composable
internal fun RainShowersIcon(
    modifier: Modifier = Modifier,
    intensity: Intensity,
    isNight: Boolean = false,
    precipitationProbability: Int? = null,
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val side = min(this.maxWidth, this.maxHeight)

        ClearIcon(
            modifier = Modifier
                .size(side * 0.7f)
                .offset(
                    x = side * 0.2f,
                    y = side * -0.25f
                ),
            isNight = isNight
        )

        CloudIcon(
            modifier = Modifier
                .size(side * 0.35f)
                .offset(
                    x = side * -0.32f,
                    y = side * -0.38f
                ),
        )

        RainIcon(
            modifier = Modifier
                .size(side * 0.78f)
                .offset(
                    x = side * 0.1f,
                    y = side * 0.08f
                ),
            intensity = intensity,
        )
        precipitationProbability?.let {
            PrecipitationProbabilityIcon(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(
                        y = side * 0.4f
                    ),
                it,
                side
            )
        }
    }
}


@Preview
@Composable
fun RainShowersPreview() {
    WeatherTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RainShowersIcon(
                modifier = Modifier
                    .size(128.dp),
                intensity = Intensity.MODERATE,
                precipitationProbability = 10
            )
        }
    }
}