package com.contraomnese.weather.core.ui.composition

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

data class WeatherConditionResources(
    @DrawableRes val backgroundResId: Int,
    val color: Color,
    @DrawableRes val iconResId: Int,
)