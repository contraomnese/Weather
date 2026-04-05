package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun RainDropCircleIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFFFFFF),
    rainDropRatio: Float = 2f,
) {
    Canvas(modifier) {
        val rainDropSize: Float = size.minDimension / rainDropRatio.coerceIn(2f, 4f)

        drawCircle(
            color = color,
            radius = rainDropSize
        )
    }
}

@Preview
@Composable
private fun RainDropCircleIconPreview() {
    WeatherTheme {
        RainDropCircleIcon(Modifier.size(200.dp))
    }
}