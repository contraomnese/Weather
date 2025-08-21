package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WeatherIcon(code: Int, modifier: Modifier = Modifier, isNight: Boolean = false) {
    when (code) {
        // ☀️ Ясно
        1000 -> SunnyIcon(modifier, isNight)

        // 🌤 Частично облачно
        1003 -> PartlyCloudyIcon(modifier, isNight)

        // ☁️ Облачно
        1006, 1009 -> CloudyIcon(modifier)

        // 🌫 Туман
        1030, 1135, 1147 -> FogIcon(modifier)

        // 🌧 Дождь
        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195,
        1240, 1243, 1246, 1273, 1276,
            -> RainIcon(modifier)

        // ❄️ Снег
        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225,
        1255, 1258, 1279, 1282,
            -> SnowIcon(modifier)

        // ⚡ Гроза
        1087 -> ThunderIcon(modifier)

        // 🧊 Лед / мокрый снег
        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207,
        1237, 1249, 1252, 1261, 1264,
            -> SleetIcon(modifier)

        else -> CloudyIcon(modifier)
    }
}

@Composable
fun SunnyIcon(modifier: Modifier = Modifier, isNight: Boolean = false) {
    Canvas(modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 4

        drawCircle(
            color = if (isNight) Color(0xFFAAA77B) else Color(0xFFF8D74A),
            radius = radius,
            center = center
        )

        if (!isNight) {
            repeat(8) { i ->
                val angle = Math.toRadians((i * 45).toDouble())
                val start = Offset(
                    x = center.x + cos(angle).toFloat() * radius * 1.3f,
                    y = center.y + sin(angle).toFloat() * radius * 1.3f
                )
                val end = Offset(
                    x = center.x + cos(angle).toFloat() * radius * 1.8f,
                    y = center.y + sin(angle).toFloat() * radius * 1.8f
                )
                drawLine(Color(0xFFF8D74A), start, end, strokeWidth = 6f, cap = StrokeCap.Round)
            }
        }
    }
}

@Composable
fun CloudyIcon(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height

        drawCircle(Color.LightGray, radius = w / 4, center = Offset(w * 0.3f, h * 0.6f))
        drawCircle(Color.LightGray, radius = w / 3, center = Offset(w * 0.5f, h * 0.5f))
        drawCircle(Color.LightGray, radius = w / 4, center = Offset(w * 0.7f, h * 0.6f))
    }
}

@Composable
fun PartlyCloudyIcon(
    modifier: Modifier = Modifier,
    isNight: Boolean = false,
    cloudColor: Color = Color.LightGray,
    luminaryColor: Color = if (isNight) Color(0xFFAAA77B) else Color(0xFFF8D74A),
    sunStroke: Float = 4f,
) {

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val center = Offset(size.width / 2 + size.width / 6, size.height / 3)
        val radius = size.minDimension / 6

        drawCircle(
            color = luminaryColor,
            radius = radius,
            center = center
        )

        if (!isNight) {
            repeat(8) { i ->
                val angle = Math.toRadians((i * 45).toDouble())
                val start = Offset(
                    x = center.x + cos(angle).toFloat() * radius * 1.3f,
                    y = center.y + sin(angle).toFloat() * radius * 1.3f
                )
                val end = Offset(
                    x = center.x + cos(angle).toFloat() * radius * 1.8f,
                    y = center.y + sin(angle).toFloat() * radius * 1.8f
                )
                drawLine(luminaryColor, start, end, strokeWidth = sunStroke, cap = StrokeCap.Round)
            }
        }

        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.3f, h * 0.6f))
        drawCircle(cloudColor, radius = w / 3, center = Offset(w * 0.5f, h * 0.5f))
        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.7f, h * 0.6f))

    }
}

@Composable
fun RainIcon(
    modifier: Modifier = Modifier,
    cloudColor: Color = Color.LightGray,
    rainDropColor: Color = Color(0xFF336D97),
    rainDropWidth: Float = 4f,
) {
    Canvas(modifier) {

        val w = size.width
        val h = size.height

        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.3f, h * 0.4f))
        drawCircle(cloudColor, radius = w / 3, center = Offset(w * 0.5f, h * 0.3f))
        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.7f, h * 0.4f))

        repeat(4) { i ->
            val x = w * (0.35f + i * 0.1f)
            drawLine(
                color = rainDropColor,
                start = Offset(x, h * 0.7f),
                end = Offset(x - 5, h * 0.8f),
                strokeWidth = rainDropWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun SnowIcon(
    modifier: Modifier = Modifier,
    cloudColor: Color = Color.LightGray,
    snowColor: Color = Color.White,
) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height

        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.3f, h * 0.4f))
        drawCircle(cloudColor, radius = w / 3, center = Offset(w * 0.5f, h * 0.3f))
        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.7f, h * 0.4f))

        repeat(3) { i ->
            val x = w * (0.37f + i * 0.15f)
            drawCircle(snowColor, radius = 4f, center = Offset(x, h * 0.75f))
        }
        repeat(3) { i ->
            val x = w * (0.3f + i * 0.15f)
            drawCircle(snowColor, radius = 4f, center = Offset(x, h * 0.85f))
        }
    }
}

@Composable
fun ThunderIcon(
    modifier: Modifier = Modifier,
    cloudColor: Color = Color.LightGray,
    thunderColor: Color = Color.Yellow,
) {

    Canvas(modifier) {
        val w = size.width
        val h = size.height

        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.3f, h * 0.4f))
        drawCircle(cloudColor, radius = w / 3, center = Offset(w * 0.5f, h * 0.3f))
        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.7f, h * 0.4f))

        drawPath(
            path = Path().apply {
                moveTo(w * 0.5f, h * 0.85f)
                lineTo(w * 0.35f, h * 0.85f)
                lineTo(w * 0.45f, h * 0.7f)
                lineTo(w * 0.55f, h * 0.7f)
                lineTo(w * 0.5f, h * 0.8f)
                lineTo(w * 0.6f, h * 0.8f)
                lineTo(w * 0.35f, h * 1f)
                lineTo(w * 0.45f, h * 0.8f)
            },
            color = thunderColor,
        )
    }
}

@Composable
fun FogIcon(
    modifier: Modifier = Modifier,
    fogColor: Color = Color.LightGray,
) {

    Canvas(
        modifier
    ) {
        val w = size.width * 0.8f
        val h = size.height

        fun Path.wave(y: Float, amplitude: Float = 6f, wavelength: Float = w / 4f) {
            moveTo(w * 0.1f, y)
            var x = w * 0.1f
            while (x < w) {
                quadraticTo(
                    x + wavelength / 2f, y - amplitude,
                    x + wavelength, y
                )
                quadraticTo(
                    x + (wavelength + (wavelength / 2f)), y + amplitude,
                    x + wavelength * 2, y
                )
                x += wavelength * 2
            }
        }

        val path1 = Path().apply { wave(h * 0.4f) }
        val path2 = Path().apply { wave(h * 0.5f) }
        val path3 = Path().apply { wave(h * 0.6f) }

        drawPath(path1, fogColor, style = Stroke(width = 3f))
        drawPath(path2, fogColor, style = Stroke(width = 3f))
        drawPath(path3, fogColor, style = Stroke(width = 3f))
    }
}

@Composable
fun SleetIcon(
    modifier: Modifier = Modifier,
    cloudColor: Color = Color.LightGray,
    snowColor: Color = Color.White,
    snowFlakeSize: Float = 3f,
    rainDropColor: Color = Color(0xFF336D97),
    rainDropWidth: Float = 3f,
) {

    Canvas(
        modifier
    ) {
        val w = size.width
        val h = size.height

        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.3f, h * 0.4f))
        drawCircle(cloudColor, radius = w / 3, center = Offset(w * 0.5f, h * 0.3f))
        drawCircle(cloudColor, radius = w / 4, center = Offset(w * 0.7f, h * 0.4f))

        repeat(2) { i ->
            val x = w * (0.25f + i * .1f)
            drawCircle(snowColor, radius = snowFlakeSize, center = Offset(x + i * 5, h * 0.75f))
        }
        repeat(2) { i ->
            val x = w * (0.55f + i * 0.15f)
            drawLine(
                color = rainDropColor,
                start = Offset(x + 5, h * 0.7f),
                end = Offset(x, h * 0.8f),
                strokeWidth = rainDropWidth,
                cap = StrokeCap.Round
            )
        }
    }
}


@Composable
@Preview
private fun ThunderIconPreview() {
    WeatherTheme {

        val sizeIcon = 32.dp

        Column {
            SunnyIcon(modifier = Modifier.size(sizeIcon))
            SunnyIcon(modifier = Modifier.size(sizeIcon), isNight = true)
            CloudyIcon(modifier = Modifier.size(sizeIcon))
            PartlyCloudyIcon(modifier = Modifier.size(sizeIcon))
            RainIcon(modifier = Modifier.size(sizeIcon))
            ThunderIcon(modifier = Modifier.size(sizeIcon))
            SnowIcon(modifier = Modifier.size(sizeIcon))
            FogIcon(modifier = Modifier.size(sizeIcon))
            SleetIcon(modifier = Modifier.size(sizeIcon))
        }
    }
}