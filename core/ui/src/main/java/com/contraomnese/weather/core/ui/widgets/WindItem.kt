package com.contraomnese.weather.core.ui.widgets

import android.graphics.Color
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
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun WindItem(
    modifier: Modifier = Modifier,
    windDegree: Int,
    windSpeed: Int,
) {
    val density = LocalDensity.current
    var width by remember(key1 = windDegree, key2 = windSpeed) { mutableIntStateOf(0) }
    val textMeasurer = rememberTextMeasurer()

    val windSpeedTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.wind_speed_title, windSpeed)),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = ComposeColor.White,
            fontSize = with(density) { (width / 8).toSp() },
            lineBreak = LineBreak.Heading,
            textAlign = TextAlign.Center
        ),
        maxLines = 2,
        constraints = Constraints(maxWidth = width / 3)
    )

    val lettersStyle = MaterialTheme.typography.headlineMedium.copy(
        color = ComposeColor.White.copy(alpha = 0.3f),
        fontSize = with(density) { (width / 8).toSp() },
    )

    val northTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.cardinal_point_n)),
        style = lettersStyle
    )

    val southTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.cardinal_point_s)),
        style = lettersStyle
    )

    val westTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.cardinal_point_w)),
        style = lettersStyle
    )

    val eastTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.cardinal_point_e)),
        style = lettersStyle
    )

    Canvas(modifier = modifier.onSizeChanged {
        width = it.width
    }) {
        val w = size.width
        val h = size.height
        val center = Offset(x = w / 2f, y = h / 2f)
        val radius = minOf(w, h) / 3f
        val shortLineRadius = radius * 0.9f
        val longLineRadius = radius * 0.8f
        val endArrowSize = w / 20
        val strokeLinePath = w / 250
        val strokeArrowPath = w / 100

        drawText(
            windSpeedTextLayout,
            topLeft = Offset(
                (size.width - windSpeedTextLayout.size.width) / 2,
                (size.height - windSpeedTextLayout.size.height) / 2
            )
        )

        drawText(
            northTextLayout, topLeft = Offset(
                center.x - northTextLayout.size.width / 2,
                (center.y - radius) - northTextLayout.size.height
            )
        )

        drawText(
            southTextLayout, topLeft = Offset(
                center.x - southTextLayout.size.width / 2,
                (center.y + radius)
            )
        )

        drawText(
            eastTextLayout, topLeft = Offset(
                (center.x + radius) + eastTextLayout.size.width / 2,
                (center.y) - eastTextLayout.size.height / 2
            )
        )

        drawText(
            westTextLayout, topLeft = Offset(
                (center.x - radius) - westTextLayout.size.width - eastTextLayout.size.width / 2,
                (center.y) - westTextLayout.size.height / 2
            )
        )

        drawContext.canvas.nativeCanvas.apply {
            val longLinePathPaint = Paint().apply {
                color = Color.argb(130, 255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = strokeLinePath
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            }

            val shortLinePathPaint = Paint().apply {
                color = Color.argb(80, 255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = strokeLinePath
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            }

            for (i in 0 until 360 step 5) {
                val rad = i * Math.PI / 180
                if (i % 45 == 0) {
                    val pathLine = Path().apply {
                        val outerPoint = PointF(
                            (center.x + radius * cos(rad)).toFloat(),
                            (center.y + radius * sin(rad)).toFloat()
                        )
                        val innerPoint = PointF(
                            (center.x + longLineRadius * cos(rad)).toFloat(),
                            (center.y + longLineRadius * sin(rad)).toFloat()
                        )
                        moveTo(outerPoint.x, outerPoint.y)
                        lineTo(innerPoint.x, innerPoint.y)
                    }
                    drawPath(pathLine, longLinePathPaint)
                } else {
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
        }

        drawContext.canvas.nativeCanvas.apply {
            val arrowPathPaint = Paint().apply {
                color = Color.rgb(255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = strokeArrowPath
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            }

            val radiusArrowEnd = (radius - shortLineRadius) / 2

            val endArrowWindRad = (windDegree - 90) * Math.PI / 180
            val startArrowWindRad = (180 + (windDegree - 90)) * Math.PI / 180

            val arrowCircleCenter = PointF(
                (center.x + (shortLineRadius + radiusArrowEnd) * cos(startArrowWindRad)).toFloat(),
                (center.y + (shortLineRadius + radiusArrowEnd) * sin(startArrowWindRad)).toFloat()
            )

            val arrowCirclePath = Path().apply {
                addCircle(
                    arrowCircleCenter.x,
                    arrowCircleCenter.y,
                    radiusArrowEnd,
                    Path.Direction.CW
                )
            }
            drawPath(arrowCirclePath, arrowPathPaint)

            val startArrowPath = Path().apply {
                val startArrowPoint = PointF(
                    (center.x + shortLineRadius * cos(startArrowWindRad)).toFloat(),
                    (center.y + shortLineRadius * sin(startArrowWindRad)).toFloat()
                )
                val endArrowPoint = PointF(
                    (center.x + shortLineRadius / 2 * cos(startArrowWindRad)).toFloat(),
                    (center.y + shortLineRadius / 2 * sin(startArrowWindRad)).toFloat()
                )

                moveTo(startArrowPoint.x, startArrowPoint.y)
                lineTo(endArrowPoint.x, endArrowPoint.y)
            }
            drawPath(startArrowPath, arrowPathPaint)

            val endArrowPath = Path().apply {
                val startArrowPoint = PointF(
                    (center.x + radius * cos(endArrowWindRad)).toFloat(),
                    (center.y + radius * sin(endArrowWindRad)).toFloat()
                )
                val endArrowPoint = PointF(
                    (center.x + shortLineRadius / 2 * cos(endArrowWindRad)).toFloat(),
                    (center.y + shortLineRadius / 2 * sin(endArrowWindRad)).toFloat()
                )

                moveTo(startArrowPoint.x, startArrowPoint.y)
                lineTo(endArrowPoint.x, endArrowPoint.y)

                val dx = startArrowPoint.x - endArrowPoint.x
                val dy = startArrowPoint.y - endArrowPoint.y
                val len = sqrt(dx * dx + dy * dy)

                val ux = dx / len
                val uy = dy / len

                val angle = Math.toRadians(20.0)

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

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
private fun WindItemPreview() {
    WeatherTheme {
        WindItem(
            modifier = Modifier.size(400.dp),
            windDegree = 54,
            windSpeed = 7
        )
    }
}

@Preview()
@Composable
private fun WindItemPreview2() {
    WeatherTheme {
        WindItem(
            modifier = Modifier.size(200.dp),
            windDegree = 57,
            windSpeed = 5
        )
    }
}
