package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
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
fun PartlyCloudIcon(
    modifier: Modifier = Modifier,
    isNight: Boolean = false,
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val side = min(this.maxWidth, this.maxHeight)

        ClearIcon(
            modifier = Modifier
                .size(side * 0.7f)
                .offset(
                    x = side * 0.2f,
                    y = side * -0.05f
                ),
            isNight = isNight
        )

        CloudIcon(
            modifier = Modifier
                .size(side * 0.5f)
                .offset(
                    x = side * -0.25f,
                    y = side * -0.15f
                )
        )

        CloudIcon(
            modifier = Modifier
                .size(side * 0.7f)
                .offset(
                    x = side * 0.15f,
                    y = side * 0.25f
                )
        )
    }
}

@Preview
@Composable
fun PartlyCloudIconPreview() {
    WeatherTheme {
        PartlyCloudIcon(
            modifier = Modifier
                .size(200.dp)
        )
    }
}

@Preview
@Composable
fun PartlyCloudIconMoonPreview() {
    WeatherTheme {
        PartlyCloudIcon(
            modifier = Modifier
                .size(200.dp),
            isNight = true
        )
    }
}