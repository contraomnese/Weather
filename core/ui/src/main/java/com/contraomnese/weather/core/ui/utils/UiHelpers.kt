package com.contraomnese.weather.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.domain.app.model.TemperatureUnit

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

fun Float.toCelsius(unit: TemperatureUnit): Float = when (unit) {
    TemperatureUnit.Celsius -> this
    TemperatureUnit.Fahrenheit -> (this - 32f) / 1.8f
}