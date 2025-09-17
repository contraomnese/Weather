package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun FogIcon(
    modifier: Modifier = Modifier,
    fogColor: Color = Color.LightGray,
) {
    Canvas(
        modifier
    ) {
        val w = size.width * 0.8f
        val h = size.height
        val fogWidth = size.minDimension / 16f
        val fogStroke = Stroke(fogWidth, cap = StrokeCap.Round)

        fun Path.wave(y: Float, amplitude: Float = size.minDimension / 12, wavelength: Float = w / 4f) {
            moveTo(w * 0.1f, y)
            var x = w * 0.1f
            while (x < w) {
                quadraticTo(
                    x + wavelength / 2f, y - amplitude,
                    x + wavelength, y
                )
                quadraticTo(
                    x + (wavelength + (wavelength / 2f)), y + amplitude,
                    x + wavelength * 2, y
                )
                x += wavelength * 2
            }
        }

        val path1 = Path().apply { wave(h * 0.3f) }
        val path2 = Path().apply { wave(h * 0.5f) }
        val path3 = Path().apply { wave(h * 0.7f) }

        drawPath(path1, fogColor, style = fogStroke)
        drawPath(path2, fogColor, style = fogStroke)
        drawPath(path3, fogColor, style = fogStroke)
    }
}

@Preview
@Composable
fun FogIconPreview() {
    WeatherTheme {
        FogIcon(Modifier.size(200.dp))
    }
}