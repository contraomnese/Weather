package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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
fun RainDropLineIcon(
    modifier: Modifier = Modifier,
    rainDropColor: Color = Color(0xFFFFFFFF),
    rainDropRatio: Float = 2f,
) {
    Canvas(modifier.fillMaxSize()) {
        val padding = size.minDimension * 0.05f
        val w = size.width
        val h = size.height
        val rainDropSize: Float = size.minDimension / rainDropRatio.coerceIn(2f, 4f)

        drawLine(
            color = rainDropColor,
            start = Offset(w - (rainDropSize / 2 + padding), rainDropSize / 2 + padding),
            end = Offset(rainDropSize / 2 + padding, h - (rainDropSize / 2 + padding)),
            strokeWidth = rainDropSize,
            cap = StrokeCap.Round
        )
    }
}

@Preview
@Composable
private fun RainDropLineIconPreview() {
    WeatherTheme {
        RainDropLineIcon(Modifier.size(200.dp))
    }
}