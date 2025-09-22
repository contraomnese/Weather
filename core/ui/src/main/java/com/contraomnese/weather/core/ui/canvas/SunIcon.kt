package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin

private const val fullRotation = 360f

@Composable
fun SunIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFF8D74A),
    rayAmount: Int = 8,
) {
    Canvas(modifier) {
        val radius = size.minDimension / 4
        val outRayRadius = radius * 1.8f
        val inRayRadius = radius * 1.4f
        val rayWidth = radius / 4

        drawCircle(
            color = color,
            radius = radius,
            center = center
        )
        repeat(rayAmount) { i ->
            val angle = Math.toRadians((i * fullRotation / rayAmount).toDouble())
            val start = Offset(
                x = center.x + cos(angle).toFloat() * inRayRadius,
                y = center.y + sin(angle).toFloat() * inRayRadius
            )
            val end = Offset(
                x = center.x + cos(angle).toFloat() * outRayRadius,
                y = center.y + sin(angle).toFloat() * outRayRadius
            )
            drawLine(color, start, end, strokeWidth = rayWidth, cap = StrokeCap.Round)
        }
    }
}

@Preview
@Composable
private fun SunIconPreview() {
    WeatherTheme {
        SunIcon(Modifier.size(200.dp))
    }
}