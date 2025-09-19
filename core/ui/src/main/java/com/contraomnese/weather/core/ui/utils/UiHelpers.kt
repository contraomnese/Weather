package com.contraomnese.weather.core.ui.utils

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.domain.app.model.TemperatureUnit

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

fun Float.toCelsius(unit: TemperatureUnit): Float = when (unit) {
    TemperatureUnit.Celsius -> this
    TemperatureUnit.Fahrenheit -> (this - 32f) / 1.8f
}

fun extractBottomColor(bitmap: Bitmap): Color {
    val y = bitmap.height - 1
    val pixels = IntArray(bitmap.width)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, y, bitmap.width, 1)

    val avgRed = pixels.map { (it shr 16) and 0xFF }.average().toInt()
    val avgGreen = pixels.map { (it shr 8) and 0xFF }.average().toInt()
    val avgBlue = pixels.map { it and 0xFF }.average().toInt()

    return Color(avgRed, avgGreen, avgBlue)
}