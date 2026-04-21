package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun CloudyIcon(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        CloudIcon(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        )
    }
}

@Preview
@Composable
fun CloudyPreview() {
    WeatherTheme {
        CloudyIcon(
            modifier = Modifier
                .height(200.dp)
                .width(64.dp)
        )
    }
}