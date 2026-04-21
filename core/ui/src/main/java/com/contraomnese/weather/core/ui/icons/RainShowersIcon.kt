package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.ClearIcon
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
                .size(side * 0.5f)
                .offset(
                    x = side * 0.25f,
                    y = side * -0.15f
                ),
            isNight = isNight
        )

        RainIcon(
            modifier = Modifier
                .size(side * 0.5f)
                .offset(
                    x = side * -0.25f,
                    y = side * -0.25f
                ),
            intensity = intensity,
        )

        RainIcon(
            modifier = Modifier
                .size(side * 0.7f)
                .offset(
                    x = side * 0.15f,
                    y = side * 0.15f
                ),
            intensity = intensity,
            precipitationProbability = precipitationProbability
        )
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
                    .width(62.dp)
                    .height(32.dp),
                intensity = Intensity.MODERATE,
                precipitationProbability = 10
            )
            RainShowersIcon(
                modifier = Modifier
                    .width(32.dp)
                    .height(64.dp),
                intensity = Intensity.MODERATE
            )
        }
    }
}