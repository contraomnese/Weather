package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
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
    color: Color = Color.White,
) {
    Canvas(modifier = modifier.aspectRatio(1.7f)) {
        val w = size.width
        val h = size.height

        val baseR = h * 0.4f
        val centerY = h * 0.65f

        val circles = listOf(
            baseR * 0.8f to Offset(w * 0.2f, centerY),
            baseR * 1.1f to Offset(w * 0.35f, centerY - baseR * 0.3f),
            baseR * 1.2f to Offset(w * 0.60f, centerY - baseR * 0.4f),
            baseR * 0.9f to Offset(w * 0.78f, centerY - baseR * 0.1f)
        )

        circles.forEach { (radius, center) ->
            drawCircle(color, radius, center)
        }

        val left = circles.first().second.x
        val right = circles.last().second.x

        drawRect(
            color = color,
            topLeft = Offset(left, centerY),
            size = Size(right - left, circles.first().first)
        )
    }
}

@Preview
@Composable
private fun CloudIconPreview() {
    WeatherTheme {
        CloudIcon(Modifier.size(128.dp))
    }
}