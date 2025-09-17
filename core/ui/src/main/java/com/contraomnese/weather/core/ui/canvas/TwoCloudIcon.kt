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
fun TwoCloudIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {

        CloudIcon(
            modifier = Modifier
                .size(maxWidth * 0.9f)
                .align(Alignment.Center)
                .offset(x = maxWidth * 0.04f, y = (-maxHeight * 0.1f)),
            cloudColor = Color(0xFF97A2AC),
            cloudCenterY = 0.6f
        )
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
fun TwoCloudPreview() {
    WeatherTheme {
        TwoCloudIcon(modifier = Modifier.size(200.dp))
    }
}