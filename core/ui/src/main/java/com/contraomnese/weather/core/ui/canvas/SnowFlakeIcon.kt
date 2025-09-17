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
fun SnowFlakeIcon(
    modifier: Modifier = Modifier,
    snowFlakeColor: Color = Color(0xFFFFFFFF),
    snowFlakeWidthRatio: Float = 6f,
) {
    Canvas(modifier) {

        val snowFlakeWidth: Float = size.minDimension / snowFlakeWidthRatio
        val outRayRadius = size.minDimension / 2 - snowFlakeWidth / 2

        repeat(8) { i ->
            val angle = Math.toRadians((i * fullRotation / 8).toDouble())
            val start = center
            val end = Offset(
                x = center.x + cos(angle).toFloat() * outRayRadius,
                y = center.y + sin(angle).toFloat() * outRayRadius
            )
            drawLine(snowFlakeColor, start, end, strokeWidth = snowFlakeWidth, cap = StrokeCap.Round)
        }
    }
}

@Preview
@Composable
fun SnowFlakeIconPreview() {
    WeatherTheme {
        SnowFlakeIcon(Modifier.size(200.dp))
    }
}