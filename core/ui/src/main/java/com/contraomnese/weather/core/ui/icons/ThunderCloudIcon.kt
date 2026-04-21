package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.core.ui.canvas.CloudIcon
import com.contraomnese.weather.core.ui.canvas.ThunderIcon
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun ThunderCloudIcon(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        val side = min(this.maxWidth, this.maxHeight)
        CloudIcon(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF97A2AC)
        )
        ThunderIcon(
            modifier = Modifier
                .size(side * .45f)
                .offset(
                    y = side * 0.5f
                ),
            thunderColor = Color(0xFFF1C312)
        )
    }
}

@Preview
@Composable
fun ThunderCloudIconPreview() {
    WeatherTheme {
        Column {
            ThunderCloudIcon(
                modifier = Modifier.size(32.dp)
            )
            ThunderCloudIcon(
                modifier = Modifier
                    .width(64.dp)
                    .height(128.dp)
            )
            ThunderCloudIcon(
                modifier = Modifier.size(128.dp)
            )
        }
    }
}