package com.contraomnese.weather.core.ui.canvas

import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val atmosphericPressure = 760

@Composable
fun Pressure(
    modifier: Modifier = Modifier,
    pressure: Int,
) {
    val density = LocalDensity.current
    var width by remember(pressure) { mutableIntStateOf(0) }
    val textMeasurer = rememberTextMeasurer()
    val degree = remember { pressure - atmosphericPressure }

    val pressureTextLayout = textMeasurer.measure(
        text = AnnotatedString(pressure.toString()),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = Color.White,
            fontSize = with(density) { (width / 8).toSp() },
            lineBreak = LineBreak.Heading,
            textAlign = TextAlign.Center
        ),
        maxLines = 1,
        constraints = Constraints(maxWidth = width / 3)
    )

    val pressureUnitsTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.pressure_mm_units)),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = Color.White,
            fontSize = with(density) { (width / 14).toSp() },
            lineBreak = LineBreak.Heading,
            textAlign = TextAlign.Center
        ),
        maxLines = 1,
        constraints = Constraints(maxWidth = width / 3)
    )

    Canvas(modifier = modifier.onSizeChanged {
        width = it.width
    }) {
        val w = size.width
        val h = size.height
        val center = Offset(x = w / 2f, y = h / 2f)
        val radius = minOf(w, h) / 3f
        val shortLineRadius = radius * 0.85f
        val endArrowSize = w / 20
        val strokeLinePath = w / 250
        val strokeArrowPath = w / 100

        drawText(
            pressureTextLayout,
            topLeft = Offset(
                (size.width - pressureTextLayout.size.width) / 2,
                (size.height - pressureUnitsTextLayout.size.height - pressureTextLayout.size.height) / 2
            )
        )

        drawText(
            pressureUnitsTextLayout,
            topLeft = Offset(
                (size.width - pressureUnitsTextLayout.size.width) / 2,
                (size.height + pressureTextLayout.size.height - pressureUnitsTextLayout.size.height) / 2
            )
        )

        drawContext.canvas.nativeCanvas.apply {

            val shortLinePathPaint = Paint().apply {
                color = android.graphics.Color.argb(80, 255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = strokeLinePath
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            }

            for (i in 135 until 405 step 5) {
                val rad = i * Math.PI / 180

                val pathLine = Path().apply {
                    val outerPoint = PointF(
                        (center.x + radius * cos(rad)).toFloat(),
                        (center.y + radius * sin(rad)).toFloat()
                    )
                    val innerPoint = PointF(
                        (center.x + shortLineRadius * cos(rad)).toFloat(),
                        (center.y + shortLineRadius * sin(rad)).toFloat()
                    )
                    moveTo(outerPoint.x, outerPoint.y)
                    lineTo(innerPoint.x, innerPoint.y)
                }
                drawPath(pathLine, shortLinePathPaint)
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            val arrowPathPaint = Paint().apply {
                color = android.graphics.Color.rgb(255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = strokeArrowPath
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            }

            val endArrowWindRad = (degree - 90) * Math.PI / 180

            val endArrowPath = Path().apply {
                val startArrowPoint = PointF(
                    (center.x + radius * cos(endArrowWindRad)).toFloat(),
                    (center.y + radius * sin(endArrowWindRad)).toFloat()
                )
                val endArrowPoint = PointF(
                    (center.x + shortLineRadius * cos(endArrowWindRad)).toFloat(),
                    (center.y + shortLineRadius * sin(endArrowWindRad)).toFloat()
                )

                moveTo(startArrowPoint.x, startArrowPoint.y)
                lineTo(endArrowPoint.x, endArrowPoint.y)
            }
            drawPath(endArrowPath, arrowPathPaint)
        }

        drawContext.canvas.nativeCanvas.apply {
            val arrowPathPaint = Paint().apply {
                color = android.graphics.Color.rgb(255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = strokeArrowPath
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            }

            val endArrowPath = Path().apply {
                val startArrowPoint = PointF(
                    center.x,
                    if (degree < 0) center.y - radius * 0.45f else center.y - radius * 0.75f
                )
                val endArrowPoint = PointF(
                    center.x,
                    if (degree < 0) center.y - radius * 0.75f else center.y - radius * 0.45f
                )

                moveTo(startArrowPoint.x, startArrowPoint.y)
                lineTo(endArrowPoint.x, endArrowPoint.y)

                val dx = startArrowPoint.x - endArrowPoint.x
                val dy = startArrowPoint.y - endArrowPoint.y
                val len = sqrt(dx * dx + dy * dy)

                val ux = dx / len
                val uy = dy / len

                val angle = Math.toRadians(30.0)

                val x1 = ux * cos(angle) - uy * sin(angle)
                val y1 = ux * sin(angle) + uy * cos(angle)

                val x2 = ux * cos(angle) + uy * sin(angle)
                val y2 = -ux * sin(angle) + uy * cos(angle)

                moveTo(startArrowPoint.x, startArrowPoint.y)
                lineTo(
                    startArrowPoint.x - (x1 * endArrowSize).toFloat(),
                    startArrowPoint.y - (y1 * endArrowSize).toFloat()
                )

                moveTo(startArrowPoint.x, startArrowPoint.y)
                lineTo(
                    startArrowPoint.x - (x2 * endArrowSize).toFloat(),
                    startArrowPoint.y - (y2 * endArrowSize).toFloat()
                )
            }
            drawPath(endArrowPath, arrowPathPaint)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187, showSystemUi = false)
@Composable
private fun WindItemPreview() {
    WeatherTheme {
        Pressure(
            modifier = Modifier.size(400.dp),
            pressure = 761
        )
    }
}

@Preview()
@Composable
private fun WindItemPreview2() {
    WeatherTheme {
        Pressure(
            modifier = Modifier.size(200.dp),
            pressure = 761
        )
    }
}