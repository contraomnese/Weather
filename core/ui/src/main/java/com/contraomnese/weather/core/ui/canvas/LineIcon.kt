package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.WeatherTheme
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun LineIcon(
    modifier: Modifier = Modifier,
) {
    val colors = WeatherTheme.weatherIconsColors

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val rayWidth = h / 8

        val startCenterLine = Offset(
            x = rayWidth / 2,
            y = h * 0.9f
        )
        val endCenterLine = Offset(
            x = w - rayWidth / 2,
            y = h * 0.9f
        )
        drawLine(colors.arrow, startCenterLine, endCenterLine, strokeWidth = rayWidth, cap = StrokeCap.Round)
    }
}

@Preview
@Composable
private fun LineIconPreview() {
    WeatherTheme {
        LineIcon(Modifier.size(200.dp))
    }
}