package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun OverCastIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        CloudIcon(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .offset(
                    x = side * 0.015f,
                    y = -side * 0.07f
                ),
            isOvercast = true,
        )
        CloudIcon(
            modifier = Modifier.fillMaxSize(0.9f),
        )
    }
}

@Preview
@Composable
fun OverCastPreview() {
    WeatherTheme {
        OverCastIcon(modifier = Modifier.size(200.dp))
    }
}