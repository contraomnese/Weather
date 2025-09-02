package com.contraomnese.weather.core.ui.canvas

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow


fun parseTimeToMinutes(timeStr: String): Int {
    val parts = timeStr.split(" ", ":")
    if (parts.size != 3) return 0
    var hour = parts[0].toIntOrNull() ?: 0
    val minute = parts[1].toIntOrNull() ?: 0
    val ampm = parts[2].uppercase()
    if (ampm == "PM" && hour < 12) hour += 12
    if (ampm == "AM" && hour == 12) hour = 0
    return hour * 60 + minute
}

@Composable
fun SunriseSunsetBezierWidget(
    modifier: Modifier = Modifier,
    sunriseMinutes: Int,
    sunsetMinutes: Int,
    currentMinutes: Int,
) {

    val progress by animateFloatAsState(
        targetValue = if (currentMinutes in sunriseMinutes..sunsetMinutes) {
            ((currentMinutes - sunriseMinutes) / (sunsetMinutes - sunriseMinutes).toFloat())
        } else if (currentMinutes > sunsetMinutes) {
            val nightDuration = 24f * 60 - sunsetMinutes + sunriseMinutes
            val nightProgress = (currentMinutes - sunsetMinutes) / nightDuration
            (-1 + nightProgress)
        } else {
            val nightDuration = 24f * 60 - sunsetMinutes + sunriseMinutes
            val nightProgress = (currentMinutes + (24 * 60 - sunsetMinutes)) / nightDuration
            (-1 + nightProgress)
        },
        animationSpec = tween(800, easing = FastOutSlowInEasing)
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val centerX = w / 2f
        val horizonY = h * 0.5f
        val arcWidth = w * 0.6f
        val arcHeight = h * 0.5f
        val strokeWidthArcPath = h * 0.03f
        val thicknessHorizon = h * 0.008f

        val leftP0 = PointF(0f - arcWidth / 2, horizonY)
        val leftP1 = PointF(0f, horizonY + arcHeight)
        val leftP2 = PointF(centerX - arcWidth / 2, horizonY)

        val rightP0 = PointF(centerX + arcWidth / 2, horizonY)
        val rightP1 = PointF(w, horizonY + arcHeight)
        val rightP2 = PointF(w + arcWidth / 2, horizonY)


        drawContext.canvas.nativeCanvas.apply {
            // MOON PATH
            val moonPaintPath = Paint().apply {
                color = Color.rgb(75, 77, 94)
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeWidth = strokeWidthArcPath
                isAntiAlias = true
            }

            val leftMoonPath = Path().apply {
                moveTo(leftP0.x, leftP0.y)
                quadTo(leftP1.x, leftP1.y, leftP2.x, leftP2.y)
            }

            drawPath(leftMoonPath, moonPaintPath)

            val rightMoonPath = Path().apply {
                moveTo(rightP0.x, rightP0.y)
                quadTo(rightP1.x, rightP1.y, rightP2.x, rightP2.y)
            }
            drawPath(rightMoonPath, moonPaintPath)

            // SUN PATH
            val sunPathPaint = Paint().apply {
                color = Color.rgb(211, 216, 220)
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeWidth = strokeWidthArcPath
                isAntiAlias = true
            }

            val sunPath = Path().apply {
                moveTo(centerX - arcWidth / 2, horizonY)
                quadTo(
                    centerX, horizonY - arcHeight,
                    centerX + arcWidth / 2, horizonY
                )
            }

            withSave {
                clipRect(0f, 0f, size.width, horizonY)
                drawPath(sunPath, sunPathPaint)
            }

        }
        drawContext.canvas.nativeCanvas.apply {
            val horizonPaint = Paint().apply {
                color = Color.rgb(211, 216, 220)
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            drawRect(RectF(0f, horizonY + thicknessHorizon, w, horizonY - thicknessHorizon), horizonPaint)
        }

        drawContext.canvas.nativeCanvas.apply {
            // DRAW SUN
            if (progress in 0.0..1.0) {
                val sunX = (1 - progress) * (1 - progress) * (centerX - arcWidth / 2) +
                        2 * (1 - progress) * progress * centerX +
                        progress * progress * (centerX + arcWidth / 2)

                val sunY = (1 - progress).pow(2) * (horizonY) +
                        2 * (1 - progress) * progress * (horizonY - arcHeight) +
                        progress.pow(2) * horizonY

                val sunRadius = min(w, h) * 0.04f
                val sunPaint = Paint().apply {
                    shader = RadialGradient(
                        sunX, sunY, sunRadius,
                        intArrayOf(Color.WHITE, Color.WHITE),
                        null, Shader.TileMode.CLAMP
                    )
                    isAntiAlias = true
                }
                drawCircle(sunX, sunY, sunRadius, sunPaint)

                val glowPaint = Paint().apply {
                    color = Color.WHITE
                    alpha = 190
                    maskFilter = BlurMaskFilter(sunRadius, BlurMaskFilter.Blur.NORMAL)
                }
                drawCircle(sunX, sunY, sunRadius, glowPaint)
            }

            // DRAW MOON ON RIGHT SIDE
            if (progress <= -0.5f) {

                val progress = abs(1 + progress)

                val moonX = ((1 - progress) * (1 - progress) * rightP0.x +
                        2 * (1 - progress) * progress * rightP1.x +
                        progress * progress * rightP2.x).coerceAtMost(w)
                val moonY = (1 - progress).pow(2) * rightP0.y +
                        2 * (1 - progress) * progress * rightP1.y +
                        progress.pow(2) * rightP2.y

                val moonRadius = min(w, h) * 0.04f
                val moonPaint = Paint().apply {
                    shader = RadialGradient(
                        moonX, moonY, moonRadius,
                        intArrayOf(
                            Color.BLACK,
                            Color.BLACK,
                            Color.rgb(255, 255, 255)
                        ),
                        floatArrayOf(0f, 0.7f, 1f), Shader.TileMode.CLAMP
                    )
                    isAntiAlias = true
                }
                drawCircle(moonX, moonY, moonRadius, moonPaint)
            }

            // DRAW MOON ON LEFT SIDE
            if (progress > -0.5f && progress < 0.0f) {

                val progress = abs(1 + progress)

                val moonX = ((1 - progress).pow(2) * leftP0.x +
                        2 * (1 - progress) * progress * leftP1.x +
                        progress * progress * leftP2.x).coerceAtLeast(0f)
                val moonY = (1 - progress).pow(2) * leftP0.y +
                        2 * (1 - progress) * progress * leftP1.y +
                        progress.pow(2) * leftP2.y

                val moonRadius = min(w, h) * 0.04f
                val moonPaint = Paint().apply {
                    shader = RadialGradient(
                        moonX, moonY, moonRadius,
                        intArrayOf(
                            Color.BLACK,
                            Color.BLACK,
                            Color.rgb(255, 255, 255)
                        ),
                        floatArrayOf(0f, 0.7f, 1f), Shader.TileMode.CLAMP
                    )
                    isAntiAlias = true
                }
                drawCircle(moonX, moonY, moonRadius, moonPaint)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
fun SunriseSunsetBezierWidgetMoonPreview() {
    WeatherTheme {
        SunriseSunsetBezierWidget(
            modifier = Modifier
                .height(200.dp)
                .width(600.dp),
            sunriseMinutes = parseTimeToMinutes("05:25 AM"),
            sunsetMinutes = parseTimeToMinutes("07:36 PM"),
            currentMinutes = parseTimeToMinutes("02:20 AM"),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2E7187)
@Composable
fun SunriseSunsetBezierWidgetSunPreview() {
    WeatherTheme {
        SunriseSunsetBezierWidget(
            modifier = Modifier
                .height(200.dp)
                .width(600.dp),
            sunriseMinutes = parseTimeToMinutes("06:25 AM"),
            sunsetMinutes = parseTimeToMinutes("05:36 PM"),
            currentMinutes = parseTimeToMinutes("03:38 PM"),
        )
    }
}
