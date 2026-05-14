package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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
fun ArrowIcon(
    modifier: Modifier = Modifier,
) {
    val colors = WeatherTheme.weatherIconsColors

    Canvas(modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val rayWidth = w / 8


        val startCenterLine = Offset(
            x = center.x,
            y = h * 0.9f
        )
        val endCenterLine = Offset(
            x = center.x,
            y = h * 0.1f
        )
        drawLine(colors.arrow, startCenterLine, endCenterLine, strokeWidth = rayWidth, cap = StrokeCap.Round)

        val yStartSideLine = h * 0.4f

        val startLeftLine = Offset(
            x = w * 0.3f,
            y = yStartSideLine
        )
        val endLeftLine = Offset(
            x = center.x,
            y = h * 0.1f
        )
        drawLine(colors.arrow, startLeftLine, endLeftLine, strokeWidth = rayWidth, cap = StrokeCap.Round)

        val startRightLine = Offset(
            x = w * 0.7f,
            y = yStartSideLine
        )
        val endRightLine = Offset(
            x = center.x,
            y = h * 0.1f
        )
        drawLine(colors.arrow, startRightLine, endRightLine, strokeWidth = rayWidth, cap = StrokeCap.Round)
    }
}

@Preview
@Composable
private fun ArrowIconPreview() {
    WeatherTheme {
        ArrowIcon(Modifier.size(200.dp))
    }
}