package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun RainCloudIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {

        val rainDropSize = maxWidth * 0.15f

        CloudIcon(
            modifier = Modifier
                .size(maxWidth)
                .align(Alignment.TopCenter),
            cloudColor = Color.White
        )
        Row(
            modifier = Modifier
                .size(maxWidth * 0.9f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(5) {
                RainDropIcon(
                    modifier = Modifier
                        .size(rainDropSize)
                        .align(Alignment.Bottom),
                    rainDropColor = Color.White,
                    rainDropWidthRatio = 3f
                )
            }
        }
    }
}

@Preview
@Composable
fun RainCloudIconPreview() {
    WeatherTheme {
        RainCloudIcon(modifier = Modifier.size(200.dp))
    }
}