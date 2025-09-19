package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun SleetCloudIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {

        val snowFlakeSize = maxWidth * 0.15f
        val rainDropSize = maxWidth * 0.15f

        CloudIcon(
            modifier = Modifier
                .size(maxWidth)
                .align(Alignment.TopCenter),
            cloudColor = Color.White
        )
        Row(
            modifier = Modifier
                .size(maxWidth * 0.75f)
                .align(Alignment.Center)
                .offset(x = -maxWidth * 0.04f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) { i ->
                if (i % 2 == 0) {
                    SnowFlakeIcon(
                        modifier = Modifier
                            .size(snowFlakeSize)
                            .align(Alignment.Bottom),
                        snowFlakeColor = Color.White,
                        snowFlakeWidthRatio = 6f
                    )
                } else {
                    RainDropIcon(
                        modifier = Modifier
                            .size(rainDropSize)
                            .align(Alignment.Bottom),
                        rainDropColor = Color.White,
                        rainDropWidthRatio = 4f
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SleetCloudIconPreview() {
    WeatherTheme {
        SleetCloudIcon(modifier = Modifier.size(200.dp))
    }
}