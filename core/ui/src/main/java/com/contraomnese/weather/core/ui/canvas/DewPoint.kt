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
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight160
import kotlinx.collections.immutable.persistentMapOf
import kotlin.math.roundToInt

sealed interface DewPointFractions {
    data object Dry : DewPointFractions
    data object VeryComfortable : DewPointFractions
    data object Comfortable : DewPointFractions
    data object ComfortableForMost : DewPointFractions
    data object Unpleasant : DewPointFractions
    data object Uncomfortable : DewPointFractions
    data object ExtremelyUncomfortable : DewPointFractions
    data object Dangerous : DewPointFractions

    companion object {
        fun from(dewPoint: Int): DewPointFractions {
            return when (dewPoint) {
                in 0..10 -> Dry
                in 11..12 -> VeryComfortable
                in 13..15 -> Comfortable
                in 16..17 -> ComfortableForMost
                in 18..20 -> Unpleasant
                in 21..23 -> Uncomfortable
                in 24..26 -> ExtremelyUncomfortable
                else -> Dangerous
            }
        }
    }
}

private val dewPointFractions = persistentMapOf(
    DewPointFractions.Dry to 1,
    DewPointFractions.VeryComfortable to 2,
    DewPointFractions.Comfortable to 3,
    DewPointFractions.ComfortableForMost to 4,
    DewPointFractions.Unpleasant to 5,
    DewPointFractions.Uncomfortable to 6,
    DewPointFractions.ExtremelyUncomfortable to 7,
    DewPointFractions.Dangerous to 8
)

@Composable
fun DewPoint(
    modifier: Modifier = Modifier,
    dewPointFraction: DewPointFractions = DewPointFractions.Dry,
) {

    val dewPoint = remember { dewPointFractions.getValue(dewPointFraction) }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val nativeCanvas = drawContext.canvas.nativeCanvas

        val thermometerWidth = canvasWidth * 0.25f
        val thermometerHeight = canvasHeight * 0.7f
        val bulbRadius = thermometerWidth / 2f
        val thermometerLeft = canvasWidth / 2f - bulbRadius
        val thermometerTop = canvasHeight * 0.05f
        val thermometerRight = thermometerLeft + thermometerWidth
        val thermometerBottom = thermometerTop + thermometerHeight
        val bulbCenterX = canvasWidth / 2f
        val bulbCenterY = thermometerBottom + bulbRadius * 0.5f

        val paintStroke = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = thermometerWidth * 0.06f
            color = Color.rgb(255, 255, 255)
        }

        val fractionPaintStroke = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = thermometerWidth * 0.1f
            strokeCap = Paint.Cap.ROUND
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
                thermometerWidth / 3,
                thermometerWidth / 3,
                Path.Direction.CW
            )
        }

        val outerThermometerBulbPath = Path().apply {
            addCircle(bulbCenterX, bulbCenterY, bulbRadius, Path.Direction.CW)
        }

        val outerThermometerPath = Path().apply {
            op(outerTermoPath, outerThermometerBulbPath, Path.Op.UNION)
        }

        val fillHeight = thermometerHeight / (dewPointFractions.size + 1) * dewPoint

        val innerThermometerPath = Path().apply {
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

        val innerThermometerFillPath = Path().apply {
            op(innerThermometerPath, innerBulbPath, Path.Op.UNION)
        }

        nativeCanvas.drawPath(innerThermometerFillPath, paintFill)
        nativeCanvas.drawPath(outerThermometerPath, paintStroke)

        val firstFractionY = thermometerBottom * 0.9f
        nativeCanvas.drawLine(
            bulbCenterX, firstFractionY,
            bulbCenterX + thermometerWidth / 2f - fractionPaintStroke.strokeWidth, firstFractionY,
            fractionPaintStroke.apply {
                color = Color.argb(180, 100, 100, 20)
            }
        )

        val startColor = Color.rgb(215, 0, 0)
        val endColor = Color.rgb(0, 215, 0)

        repeat(dewPointFractions.size - 1) { i ->

            val colorFraction = 1.0f - i.toFloat() / (dewPointFractions.size - 2)

            val red = (startColor.red * (1 - colorFraction) + endColor.red * colorFraction).roundToInt()
            val green = (startColor.green * (1 - colorFraction) + endColor.green * colorFraction).roundToInt()
            val blue = (startColor.blue * (1 - colorFraction) + endColor.blue * colorFraction).roundToInt()

            val interpolatedColor = Color.argb(180, red, green, blue)

            val y = thermometerBottom * 0.9f - (i + 1) * (thermometerHeight / (dewPointFractions.size + 1))
            nativeCanvas.drawLine(
                bulbCenterX, y,
                bulbCenterX + thermometerWidth / 2f - fractionPaintStroke.strokeWidth, y,
                fractionPaintStroke.apply {
                    color = interpolatedColor
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
private fun DewPointPreview() {
    WeatherTheme {
        DewPoint(
            modifier = Modifier.size(itemHeight160),
            dewPointFraction = DewPointFractions.from(11),
        )
    }
}