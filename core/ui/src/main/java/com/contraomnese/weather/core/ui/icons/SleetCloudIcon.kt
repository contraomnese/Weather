package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.core.ui.canvas.SnowFlakeIcon
import com.contraomnese.weather.core.ui.canvas.WaterRainDropIcon
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun SleetCloudIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        val sizeRainDrop = side * 0.15f
        CloudIcon(modifier = Modifier.fillMaxSize())

        Row(
            modifier = Modifier
                .width(side * .8f)
                .height(side * .2f)
                .offset(
                    x = -side * 0.05f,
                    y = side * 0.8f
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) { i ->
                if (i % 2 == 0) {
                    SnowFlakeIcon(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        snowFlakeColor = Color.White,
                        snowFlakeRatio = 6f
                    )
                } else {
                    WaterRainDropIcon(
                        modifier = Modifier
                            .size(sizeRainDrop)
                            .align(Alignment.CenterVertically)
                            .graphicsLayer {
                                rotationZ = 15f
                            },
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