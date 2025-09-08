package com.contraomnese.weather.core.ui.canvas

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.max
import kotlin.math.roundToInt

private const val maxDewPointFraction = 8f
private const val minDewPointFraction = 1f
private const val maxRainfallFraction = 5f

@Composable
fun DewPoint(
    modifier: Modifier = Modifier,
    dewPointFraction: Float = 1f,
    rainfallFraction: Float = 0f,
) {


    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val nativeCanvas = drawContext.canvas.nativeCanvas

        val thermometerWidth = canvasWidth * 0.25f
        val thermometerHeight = canvasHeight * 0.7f
        val bulbRadius = thermometerWidth / 2f
        val thermometerLeft = canvasWidth / 3f - bulbRadius
        val thermometerTop = canvasHeight * 0.1f
        val thermometerRight = thermometerLeft + thermometerWidth
        val thermometerBottom = thermometerTop + thermometerHeight
        val bulbCenterX = canvasWidth / 3f
        val bulbCenterY = thermometerBottom + bulbRadius * 0.5f
        val dropSize = max(canvasWidth, canvasHeight) * 0.04f

        val paintStroke = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = thermometerWidth * 0.06f
            color = Color.rgb(255, 255, 255)
        }

        val fractionPaintStroke = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = thermometerWidth * 0.05f
        }

        val paintFill = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.argb(150, 100, 150, 255)
        }

        val outerTermo = RectF(
            thermometerLeft,
            thermometerTop,
            thermometerRight,
            thermometerBottom
        )

        val outerTermoPath = Path().apply {
            addRoundRect(
                outerTermo,
                floatArrayOf(
                    0f, 0f,
                    0f, 0f,
                    bulbRadius, bulbRadius,
                    bulbRadius, bulbRadius
                ),
                Path.Direction.CW
            )
        }

        val outerTermoBulbPath = Path().apply {
            addCircle(bulbCenterX, bulbCenterY, bulbRadius, Path.Direction.CW)
        }

        val outerThermoPath = Path().apply {
            op(outerTermoPath, outerTermoBulbPath, Path.Op.UNION)
        }

        val fillHeight = thermometerHeight / (maxDewPointFraction + 1) * dewPointFraction.coerceIn(minDewPointFraction, maxDewPointFraction)

        val innerTermoPath = Path().apply {
            moveTo(thermometerLeft, thermometerBottom - fillHeight)
            lineTo(thermometerRight, thermometerBottom - fillHeight)
            lineTo(thermometerRight, thermometerBottom - thermometerWidth / 2f)
            quadTo(
                thermometerRight, thermometerBottom,
                thermometerRight - thermometerWidth / 2f, thermometerBottom
            )
            lineTo(thermometerLeft + thermometerWidth / 2f, thermometerBottom)
            quadTo(
                thermometerLeft, thermometerBottom,
                thermometerLeft, thermometerBottom - thermometerWidth / 2f
            )
            close()
        }

        val innerBulbPath = Path().apply {
            addCircle(bulbCenterX, bulbCenterY, bulbRadius * 0.95f, Path.Direction.CW)
        }

        val termoFillPath = Path().apply {
            op(innerTermoPath, innerBulbPath, Path.Op.UNION)
        }

        nativeCanvas.drawPath(termoFillPath, paintFill)
        nativeCanvas.drawPath(outerThermoPath, paintStroke)

        val startColor = Color.argb(150, 255, 0, 0)
        val endColor = Color.argb(150, 0, 255, 0)

        repeat(maxDewPointFraction.toInt()) { i ->

            val fraction = 1.0f - i.toFloat() / (maxDewPointFraction - 1)

            val red = (startColor.red * (1 - fraction) + endColor.red * fraction).roundToInt()
            val green = (startColor.green * (1 - fraction) + endColor.green * fraction).roundToInt()
            val blue = (startColor.blue * (1 - fraction) + endColor.blue * fraction).roundToInt()

            val interpolatedColor = Color.argb(150, red, green, blue)

            val y = thermometerBottom - (i + 1) * (thermometerHeight / (maxDewPointFraction + 1))
            nativeCanvas.drawLine(
                bulbCenterX, y,
                bulbCenterX + thermometerWidth / 2f - paintStroke.strokeWidth / 2, y,
                fractionPaintStroke.apply {
                    color = interpolatedColor
                }
            )
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

        repeat(maxRainfallFraction.toInt()) { i ->
            val isFilled = i < rainfallFraction

            drawDrop(canvasWidth * 0.75f, thermometerBottom + bulbRadius - (thermometerHeight * i * 0.25f), dropSize, isFilled)
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
private fun DewPointPreview() {
    WeatherTheme {
        DewPoint(
            modifier = Modifier.size(800.dp),
            dewPointFraction = 6f,
            rainfallFraction = 5f
        )
    }
}