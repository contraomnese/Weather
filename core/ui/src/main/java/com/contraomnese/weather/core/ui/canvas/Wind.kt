package com.contraomnese.weather.core.ui.canvas

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
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
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
fun Wind(
    modifier: Modifier = Modifier,
    degree: Int,
    direction: String,
) {
    val density = LocalDensity.current
    var width by remember(key1 = degree, key2 = direction) { mutableIntStateOf(0) }
    val textMeasurer = rememberTextMeasurer()

    val windDirectionTextLayout = textMeasurer.measure(
        text = AnnotatedString(direction),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = ComposeColor.White,
            fontSize = with(density) { (width / 8).toSp() },
            lineBreak = LineBreak.Heading,
            textAlign = TextAlign.Center
        ),
        maxLines = 1,
        constraints = Constraints(maxWidth = width / 2)
    )

    val lettersStyle = MaterialTheme.typography.headlineMedium.copy(
        color = ComposeColor.White.copy(alpha = 0.5f),
        fontSize = with(density) { (width / 12).toSp() },
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

    Canvas(
        modifier = modifier
            .onSizeChanged {
                width = it.width
            }
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    ) {
        val radius = size.minDimension / 2.1f
        val shortLineRadius = radius * 0.9f
        val longLineRadius = radius * 0.8f
        val endArrowSize = size.minDimension / 10
        val strokeLinePath = size.minDimension / 100
        val strokeArrowPath = size.minDimension / 40

        drawText(
            northTextLayout, topLeft = Offset(
                center.x - northTextLayout.size.width / 2,
                (center.y - longLineRadius)
            )
        )

        drawText(
            southTextLayout, topLeft = Offset(
                center.x - southTextLayout.size.width / 2,
                (center.y + longLineRadius) - southTextLayout.size.height
            )
        )

        drawText(
            eastTextLayout, topLeft = Offset(
                (center.x + longLineRadius * 0.85f),
                (center.y) - eastTextLayout.size.height / 2
            )
        )

        drawText(
            westTextLayout, topLeft = Offset(
                (center.x - longLineRadius * 0.95f),
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

            val endArrowWindRad = (degree - 90 - 180) * Math.PI / 180
            val startArrowWindRad = (180 + (degree - 90 - 180)) * Math.PI / 180

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
                    (center.x + shortLineRadius / 3 * cos(startArrowWindRad)).toFloat(),
                    (center.y + shortLineRadius / 3 * sin(startArrowWindRad)).toFloat()
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
                    (center.x + shortLineRadius / 3 * cos(endArrowWindRad)).toFloat(),
                    (center.y + shortLineRadius / 3 * sin(endArrowWindRad)).toFloat()
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

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                color = Color.TRANSPARENT
                setShadowLayer(
                    20f,
                    2f,
                    4f,
                    Color.argb(150, 0, 0, 0)
                )
            }

            canvas.nativeCanvas.drawCircle(
                center.x,
                center.y,
                radius * 0.4f,
                paint
            )

            val clearPaint = Paint().apply {
                isAntiAlias = true
                xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR)
            }

            canvas.nativeCanvas.drawCircle(
                center.x,
                center.y,
                radius * 0.4f,
                clearPaint
            )
        }

        drawContext.canvas.nativeCanvas.apply {

            drawText(
                windDirectionTextLayout,
                topLeft = Offset(
                    (size.width - windDirectionTextLayout.size.width) / 2,
                    (size.height - windDirectionTextLayout.size.height) / 2
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
private fun WindItemPreview() {
    WeatherTheme {
        Wind(
            modifier = Modifier.size(400.dp),
            degree = 54,
            direction = "ЮЮЗ"
        )
    }
}

@Preview()
@Composable
private fun WindItemPreview2() {
    WeatherTheme {
        Wind(
            modifier = Modifier.size(200.dp),
            degree = 57,
            direction = "NW"
        )
    }
}
