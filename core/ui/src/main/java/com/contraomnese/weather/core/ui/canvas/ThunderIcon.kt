package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.WeatherTheme
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun ThunderIcon(
    modifier: Modifier = Modifier,
) {
    val colors = WeatherTheme.weatherIconsColors

    Canvas(modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val thunderPath = Path().apply {
            moveTo(w * 0.45f, h * 0.55f)
            lineTo(w * 0.25f, h * 0.55f)
            lineTo(center.x, h * 0.1f)
            lineTo(w * 0.75f, h * 0.1f)
            lineTo(w * 0.6f, h * 0.4f)
            lineTo(w * 0.85f, h * 0.4f)
            lineTo(w * 0.2f, h)
        }

        drawPath(thunderPath, colors.thunder)
    }
}

@Preview
@Composable
private fun RainDropIconPreview() {
    WeatherTheme {
        ThunderIcon(Modifier.size(200.dp))
    }
}