package com.contraomnese.weather.core.ui.canvas

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlinx.collections.immutable.persistentMapOf
import kotlin.math.min

sealed interface RainfallFractions {
    data object None : RainfallFractions
    data object Light : RainfallFractions
    data object Moderate : RainfallFractions
    data object Heavy : RainfallFractions
    data object VeryHeavy : RainfallFractions
    data object Extreme : RainfallFractions

    companion object {
        fun from(rainfall: Int): RainfallFractions {
            return when (rainfall) {
                in 0..2 -> None
                in 3..4 -> Light
                in 5..10 -> Moderate
                in 12..15 -> Heavy
                in 16..25 -> VeryHeavy
                else -> Extreme
            }
        }
    }
}

private val rainfallFractions = persistentMapOf(
    RainfallFractions.None to 0,
    RainfallFractions.Light to 1,
    RainfallFractions.Moderate to 2,
    RainfallFractions.Heavy to 3,
    RainfallFractions.VeryHeavy to 4,
    RainfallFractions.Extreme to 5
)

@Composable
fun Rainfall(
    modifier: Modifier = Modifier,
    rainfallFraction: RainfallFractions = RainfallFractions.None,
) {

    val rainfall = remember { rainfallFractions.getValue(rainfallFraction) }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val nativeCanvas = drawContext.canvas.nativeCanvas

        val dropSize = min(canvasWidth, canvasHeight) * 0.1f

        val paintStroke = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dropSize * 0.1f
            color = Color.rgb(255, 255, 255)
        }

        val paintFill = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.argb(150, 100, 150, 255)
        }

        fun drawDrop(cx: Float, cy: Float, size: Float, isFilled: Boolean = true) {
            val path = Path().apply {
                moveTo(cx, cy - size * 2)
                cubicTo(
                    cx - size * 0.8f, cy - size * 0.8f,
                    cx - size, cy - size * 0.5f,
                    cx - size, cy
                )
                arcTo(
                    RectF(cx - size, cy - size, cx + size, cy + size),
                    180f, -180f
                )
                cubicTo(
                    cx + size, cy - size * 0.5f,
                    cx + size * 0.8f, cy - size * 0.8f,
                    cx, cy - size * 2
                )
                close()
            }
            if (isFilled) {
                nativeCanvas.drawPath(path, paintFill)
            }
            nativeCanvas.drawPath(path, paintStroke)
        }

        val positions = listOf(
            canvasWidth * 0.25f to (center.y - dropSize * 2),
            canvasWidth * 0.50f to (center.y - dropSize * 2),
            canvasWidth * 0.75f to (center.y - dropSize * 2),
            canvasWidth * 0.365f to (center.y + dropSize * 2),
            canvasWidth * 0.635f to (center.y + dropSize * 2)
        )

        for (i in 0 until (rainfallFractions.size - 1)) {
            val (x, y) = positions[i]
            val isFilled = i + 1 <= rainfall
            drawDrop(x, y, dropSize, isFilled)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
private fun RainfallPreview() {
    WeatherTheme {
        Rainfall(
            modifier = Modifier.size(600.dp),
            rainfallFraction = RainfallFractions.VeryHeavy
        )
    }
}