package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight8
import com.contraomnese.weather.design.theme.itemWidth112
import com.contraomnese.weather.domain.app.model.TemperatureUnit

val aqiIndexGradientStops = listOf(
    1f to Color(0xFF2D6FA1),
    2f to Color(0xFF2DA19D),
    3f to Color(0xFF2DA14E),
    5f to Color(0xFF569221),
    6f to Color(0xFFA18931),
    7f to Color(0xFFB6500F),
    8f to Color(0xFFCE2421),
    9f to Color(0xFF951111),
    10f to Color(0xFF680A05),
)

val uvIndexGradientStops = listOf(
    0f to Color(0xFF3E840D),
    3f to Color(0xFFDCC635),
    6f to Color(0xFFDC6F35),
    9f to Color(0xFFDC3B35),
    11f to Color(0xFFBD35DC),
)

val temperatureGradientStops = listOf(
    -40f to Color(0xFF511089),
    -30f to Color(0xFF7015C0),
    -20f to Color(0xFF4815C0),
    -10f to Color(0xFF3652DC),
    0f to Color(0xFF157CC0),
    10f to Color(0xFF3CC4A2),
    20f to Color(0xFF67A043),
    30f to Color(0xFFDCB835),
    40f to Color(0xFFDC5C35),
    50f to Color(0xFFDC3535),
)

private fun Float.toCelsius(unit: TemperatureUnit): Float = when (unit) {
    TemperatureUnit.Celsius -> this
    TemperatureUnit.Fahrenheit -> (this - 32f) / 1.8f
}

@Composable
fun TemperatureRangeLine(
    modifier: Modifier = Modifier,
    minRange: Float,
    maxRange: Float,
    min: Float,
    max: Float,
    current: Float? = null,
    temperatureUnit: TemperatureUnit,
) {

    GradientRangeLine(
        modifier = modifier,
        minRange = minRange.toCelsius(temperatureUnit),
        maxRange = maxRange.toCelsius(temperatureUnit),
        min = min.toCelsius(temperatureUnit),
        max = max.toCelsius(temperatureUnit),
        current = current?.toCelsius(temperatureUnit),
        gradientStops = temperatureGradientStops
    )
}

@Composable
fun UvIndexRangeLine(
    modifier: Modifier = Modifier,
    current: Float,
) {
    GradientRangeLine(
        modifier = modifier,
        minRange = 0f,
        maxRange = 11f,
        current = current,
        gradientStops = uvIndexGradientStops
    )
}

@Composable
fun AqiIndexRangeLine(
    modifier: Modifier = Modifier,
    current: Float,
) {
    GradientRangeLine(
        modifier = modifier,
        minRange = 1f,
        maxRange = 10f,
        current = current,
        gradientStops = aqiIndexGradientStops
    )
}

@Composable
private fun GradientRangeLine(
    modifier: Modifier = Modifier,
    minRange: Float,
    maxRange: Float,
    min: Float = minRange,
    max: Float = maxRange,
    current: Float? = null,
    currentColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.15f),
    gradientStops: List<Pair<Float, Color>>,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val thickness = h * 0.8f
        val centerY = h / 2f
        val cornerRadius = CornerRadius(thickness / 2, thickness / 2)
        val range = (maxRange - minRange).coerceAtLeast(1f)

        fun toX(value: Float): Float = ((value - minRange) / range).coerceIn(0f, 1f) * w

        val minX = toX(min)
        val maxX = toX(max)
        val currentX = current?.let { toX(it) }

        val stopsForBrush = buildList {
            add(0f to interpolateColor(min, gradientStops))
            for (i in 0 until gradientStops.lastIndex) {
                val (t1, _) = gradientStops[i]
                val (t2, _) = gradientStops[i + 1]
                if (max < t1 || min > t2) continue
                val sT = maxOf(min, t1)
                val eT = minOf(max, t2)
                val denom = (max - min).coerceAtLeast(1e-6f)
                val sFrac = (sT - min) / denom
                val eFrac = (eT - min) / denom
                add(sFrac to interpolateColor(sT, gradientStops))
                add(eFrac to interpolateColor(eT, gradientStops))
            }
            add(1f to interpolateColor(max, gradientStops))
        }.distinctBy { it.first }.sortedBy { it.first }

        val gradientBrush = Brush.horizontalGradient(
            colorStops = stopsForBrush.toTypedArray(),
            startX = minX,
            endX = maxX,
            tileMode = TileMode.Clamp
        )

        val drawBar: DrawScope.() -> Unit = {
            drawRoundRect(
                color = backgroundColor,
                topLeft = Offset(0f, centerY - thickness / 2),
                size = Size(w, thickness),
                cornerRadius = cornerRadius
            )
            drawRoundRect(
                brush = gradientBrush,
                topLeft = Offset(minX, centerY - thickness / 2),
                size = Size(maxX - minX, thickness),
                cornerRadius = cornerRadius
            )
        }

        if (currentX == null) {
            drawBar()
        } else {
            val hole = Path().apply {
                addOval(Rect(currentX - thickness, centerY - thickness, currentX + thickness, centerY + thickness))
            }
            clipPath(hole, clipOp = ClipOp.Difference) {
                drawBar()
            }
            drawCircle(
                color = currentColor,
                center = Offset(currentX, centerY),
                radius = thickness / 1.3f
            )
        }
    }
}

fun interpolateColor(value: Float, stops: List<Pair<Float, Color>>): Color {
    for (i in 0 until stops.lastIndex) {
        val (t1, c1) = stops[i]
        val (t2, c2) = stops[i + 1]
        if (value in t1..t2) {
            val fraction = (value - t1) / (t2 - t1)
            return lerp(c1, c2, fraction)
        }
    }
    return if (value <= stops.first().first) stops.first().second else stops.last().second
}

@Preview(showBackground = true)
@Composable
fun TemperatureRangeLinePreview() {
    WeatherTheme {
        TemperatureRangeLine(
            modifier = Modifier
                .width(itemWidth112)
                .height(itemHeight8),
            minRange = -5f,
            maxRange = 35f,
            min = 0f,
            current = 23f,
            max = 33f,
            temperatureUnit = TemperatureUnit.Celsius
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF41865E)
@Composable
fun UVIndexRangeLinePreview() {
    WeatherTheme {
        UvIndexRangeLine(
            modifier = Modifier
                .width(itemWidth112)
                .height(itemHeight8),
            current = 2f,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AqiRangeLinePreview() {
    WeatherTheme {
        AqiIndexRangeLine(
            modifier = Modifier
                .width(itemWidth112)
                .height(itemHeight8),
            current = 2f,
        )
    }
}
