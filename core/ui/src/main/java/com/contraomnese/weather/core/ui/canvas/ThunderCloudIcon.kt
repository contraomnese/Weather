package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun ThunderCloudIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {

        CloudIcon(
            modifier = Modifier
                .size(maxWidth)
                .align(Alignment.TopCenter),
            cloudColor = Color.White
        )
        ThunderIcon(
            modifier = Modifier
                .size(maxWidth * 0.4f)
                .align(Alignment.BottomCenter),
            thunderColor = Color(0xFFF1C312)
        )
    }
}

@Preview
@Composable
fun ThunderCloudIconPreview() {
    WeatherTheme {
        ThunderCloudIcon(modifier = Modifier.size(200.dp))
    }
}