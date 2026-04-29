package com.contraomnese.weather.core.ui.canvas


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.WeatherTheme

@Composable
fun WaterRainDropIcon(modifier: Modifier = Modifier) {
    val colors = WeatherTheme.weatherIconsColors

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            val topPoint = center.y - height * 0.45f
            moveTo(center.x, topPoint)

            cubicTo(
                x1 = center.x - width * 0.15f, y1 = center.y - height * 0.25f,
                x2 = center.x - width * 0.50f, y2 = center.y + height * 0.15f,
                x3 = center.x - width * 0.2f, y3 = center.y + height * 0.40f
            )

            quadraticTo(
                x1 = center.x, y1 = center.y + height * 0.55f,
                x2 = center.x + width * 0.2f, y2 = center.y + height * 0.40f
            )

            cubicTo(
                x1 = center.x + width * 0.50f, y1 = center.y + height * 0.15f,
                x2 = center.x + width * 0.15f, y2 = center.y - height * 0.25f,
                x3 = center.x, y3 = topPoint
            )

            close()
        }

        drawPath(
            path = path,
            color = colors.rainDropWater
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WaterRainDropPreview() {
    WaterRainDropIcon(
        modifier = Modifier.size(200.dp)
    )
}