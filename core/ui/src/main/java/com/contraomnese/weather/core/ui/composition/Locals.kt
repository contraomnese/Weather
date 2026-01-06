package com.contraomnese.weather.core.ui.composition

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.contraomnese.weather.design.R
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

val LocalWeatherBackgrounds = staticCompositionLocalOf<Map<WeatherCondition, WeatherBackground>> {
    error("Weather backgrounds are not provided")
}

data class WeatherBackground(
    @DrawableRes val resId: Int,
    val color: Color,
)

val weatherBackgrounds =
    mapOf(
        WeatherCondition.CLEAR to WeatherBackground(R.drawable.clear, Color(0xFF302320)),
        WeatherCondition.PARTLY_CLOUDY to WeatherBackground(R.drawable.partly_cloud, Color(0xFF788088)),
        WeatherCondition.CLOUDY to WeatherBackground(R.drawable.overcast, Color(0xFF999B9D)),
        WeatherCondition.FOG to WeatherBackground(R.drawable.fog, Color(0xFF676767)),
        WeatherCondition.RAIN to WeatherBackground(R.drawable.rain, Color(0xFF485043)),
        WeatherCondition.SNOW to WeatherBackground(R.drawable.snow, Color(0xFFE9F0F6)),
        WeatherCondition.THUNDER to WeatherBackground(R.drawable.thunder, Color(0xFF6276A0)),
        WeatherCondition.SLEET to WeatherBackground(R.drawable.sleet, Color(0xFF0F1D24)),
    )