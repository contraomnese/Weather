package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun PartlyCloudIcon(modifier: Modifier = Modifier, isNight: Boolean = false) {
    BoxWithConstraints(modifier) {
        if (isNight) {
            MoonIcon(
                modifier = Modifier
                    .size(maxWidth * 0.5f)
                    .align(Alignment.TopStart)
                    .offset(x = maxWidth * 0.01f, y = (maxHeight * 0.05f)),
            )
        } else {
            SunIcon(
                modifier = Modifier
                    .size(maxWidth * 0.5f)
                    .align(Alignment.TopEnd)
                    .offset(x = maxWidth * 0.01f, y = (maxHeight * 0.05f)),
            )
        }

        CloudIcon(
            modifier = Modifier
                .size(maxWidth * 0.9f)
                .align(Alignment.Center),
            cloudColor = Color.White,
            cloudCenterY = 0.6f
        )
    }
}

@Preview
@Composable
fun PartlyCloudIconPreview() {
    WeatherTheme {
        PartlyCloudIcon(modifier = Modifier.size(200.dp))
    }
}

@Preview
@Composable
fun PartlyCloudIconMoonPreview() {
    WeatherTheme {
        PartlyCloudIcon(modifier = Modifier.size(200.dp), isNight = true)
    }
}