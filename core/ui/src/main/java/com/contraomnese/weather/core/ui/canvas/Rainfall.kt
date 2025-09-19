package com.contraomnese.weather.core.ui.canvas

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme


@Composable
fun Rainfall(
    modifier: Modifier = Modifier,
    value: Float,
    minValue: Float = 0f,
    maxValue: Float,
) {

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val nativeCanvas = drawContext.canvas.nativeCanvas
        val topRainfallValue = interpolateRainfall(value, minValue, maxValue, canvasHeight)

        val paintFill = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.argb(150, 100, 150, 255)
        }

        nativeCanvas.drawRoundRect(
            RectF(0f, topRainfallValue, canvasWidth, canvasHeight),
            canvasWidth * 0.1f,
            canvasWidth * 0.1f,
            paintFill
        )

    }
}

private fun interpolateRainfall(value: Float, min: Float, max: Float, height: Float): Float {
    val clamped = value.coerceIn(min, max)
    val fraction = (clamped - min) / (max - min)
    return height * (1f - fraction)
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
private fun RainfallPreview() {
    WeatherTheme {
        Rainfall(
            modifier = Modifier.size(600.dp),
            value = 1.3f,
            maxValue = 15f
        )
    }
}