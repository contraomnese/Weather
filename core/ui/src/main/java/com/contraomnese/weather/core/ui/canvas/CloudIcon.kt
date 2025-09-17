package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun CloudIcon(
    modifier: Modifier = Modifier,
    cloudColor: Color = Color.White,
    cloudCenterY: Float = 0.5f,
) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val offsetY = h * cloudCenterY

        val baseClouds = listOf(
            w / 5 to Offset(w * 0.2f, 0f),
            w / 6 to Offset(w * 0.3f, -w * 0.1f),
            w / 3.5f to Offset(w * 0.55f, -w * 0.09f),
            w / 5 to Offset(w * 0.8f, 0f),
        )

        baseClouds.forEach { (radius, center) ->
            drawCircle(cloudColor, radius, center.copy(y = center.y + offsetY))
        }

        val left = baseClouds.first().second.x
        val top = offsetY
        val width = (baseClouds[3].second.x - baseClouds.first().second.x)
        val height = baseClouds.first().first

        drawRect(
            color = cloudColor,
            topLeft = Offset(left, top),
            size = Size(width, height)
        )
    }
}

@Preview
@Composable
private fun CloudIconPreview() {
    WeatherTheme {
        CloudIcon(Modifier.size(200.dp))
    }
}