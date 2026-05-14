package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.WeatherTheme
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin

private const val fullRotation = 360f

@Composable
fun SunIcon(
    modifier: Modifier = Modifier,
    rayAmount: Int = 8,
    angle: Float = 22.5f,
) {
    val colors = WeatherTheme.weatherIconsColors
    Canvas(modifier.aspectRatio(1f)) {
        val radius = size.width / 2.2f
        val inRayRadius = radius * 0.7f
        val rayWidth = radius / 8

        drawCircle(
            color = colors.sun,
            radius = radius / 1.8f,
            center = center
        )
        repeat(rayAmount) { i ->
            val angleRay = Math.toRadians((i * fullRotation / rayAmount + angle).toDouble())
            val start = Offset(
                x = center.x + cos(angleRay).toFloat() * inRayRadius,
                y = center.y + sin(angleRay).toFloat() * inRayRadius
            )
            val end = Offset(
                x = center.x + cos(angleRay).toFloat() * radius,
                y = center.y + sin(angleRay).toFloat() * radius
            )
            drawLine(colors.sun, start, end, strokeWidth = rayWidth, cap = StrokeCap.Round)
        }
    }
}

@Preview
@Composable
private fun SunIconPreview() {
    WeatherTheme {
        SunIcon(Modifier.size(128.dp))
    }
}