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

@Composable
fun RainDropIcon(
    modifier: Modifier = Modifier,
    rainDropColor: Color = Color(0xFFFFFFFF),
    rainDropWidthRatio: Float = 6f,
) {
    Canvas(modifier) {

        val w = size.width
        val h = size.height
        val rainDropWidth: Float = size.minDimension / rainDropWidthRatio

        drawLine(
            color = rainDropColor,
            start = Offset(w - rainDropWidth / 2, rainDropWidth / 2),
            end = Offset(rainDropWidth / 2, h - rainDropWidth / 2),
            strokeWidth = rainDropWidth,
            cap = StrokeCap.Round
        )
    }
}

@Preview
@Composable
private fun RainDropIconPreview() {
    WeatherTheme {
        RainDropIcon(Modifier.size(200.dp))
    }
}