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
import com.contraomnese.weather.design.IndexColor
import com.contraomnese.weather.design.WeatherTheme
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight8
import com.contraomnese.weather.design.theme.itemWidth112
import com.contraomnese.weather.domain.app.model.TemperatureUnit

private fun Int.toCelsius(unit: TemperatureUnit): Float = when (unit) {
    TemperatureUnit.Celsius -> this.toFloat()
    TemperatureUnit.Fahrenheit -> (this - 32f) / 1.8f
}

@Composable
fun TemperatureRangeLine(
    modifier: Modifier = Modifier,
    minRange: Int,
    maxRange: Int,
    min: Int,
    max: Int,
    current: Int? = null,
    temperatureUnit: TemperatureUnit,
) {
    val gradientStops = WeatherTheme.temperatureGradientIndex
    GradientRangeLine(
        modifier = modifier,
        minRange = minRange.toCelsius(temperatureUnit),
        maxRange = maxRange.toCelsius(temperatureUnit),
        min = min.toCelsius(temperatureUnit),
        max = max.toCelsius(temperatureUnit),
        current = current?.toCelsius(temperatureUnit),
        gradientStops = gradientStops
    )
}

@Composable
fun UvIndexRangeLine(
    modifier: Modifier = Modifier,
    current: Int,
) {
    val gradientStops = WeatherTheme.uvIndexGradientIndex
    GradientRangeLine(
        modifier = modifier,
        minRange = 0f,
        maxRange = 11f,
        current = current.toFloat(),
        gradientStops = gradientStops
    )
}

@Composable
fun AqiIndexRangeLine(
    modifier: Modifier = Modifier,
    current: Int,
) {
    val gradientStops = WeatherTheme.aqiGradientIndex
    GradientRangeLine(
        modifier = modifier,
        minRange = 1f,
        maxRange = 10f,
        current = current.toFloat(),
        gradientStops = gradientStops
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
    gradientStops: List<IndexColor>,
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

fun interpolateColor(value: Float, stops: List<IndexColor>): Color {
    for (i in 0 until stops.lastIndex) {
        val (t1, c1) = stops[i]
        val (t2, c2) = stops[i + 1]
        if (value in t1..t2) {
            val fraction = (value - t1) / (t2 - t1)
            return lerp(c1, c2, fraction)
        }
    }
    return if (value <= stops.first().level) stops.first().color else stops.last().color
}

@Preview(showBackground = true)
@Composable
fun TemperatureRangeLinePreview() {
    WeatherTheme {
        TemperatureRangeLine(
            modifier = Modifier
                .width(itemWidth112)
                .height(itemHeight8),
            minRange = -5,
            maxRange = 35,
            min = 0,
            current = 23,
            max = 33,
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
            current = 2,
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
            current = 2,
        )
    }
}
