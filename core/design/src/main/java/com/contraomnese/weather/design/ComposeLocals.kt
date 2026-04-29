package com.contraomnese.weather.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAirQualityColors = staticCompositionLocalOf {
    AirQualityColors(
        good = Color.Unspecified,
        moderate = Color.Unspecified,
        unhealthySens = Color.Unspecified,
        unhealthy = Color.Unspecified,
        veryUnhealthy = Color.Unspecified,
        hazardous = Color.Unspecified
    )
}

val LocalAqiGradientColors = staticCompositionLocalOf {
    listOf(
        IndexColor(1f, Color(0xFF2D6FA1)),
        IndexColor(2f, Color(0xFF2DA19D)),
        IndexColor(3f, Color(0xFF2DA14E)),
        IndexColor(5f, Color(0xFF569221)),
        IndexColor(6f, Color(0xFFA18931)),
        IndexColor(7f, Color(0xFFB6500F)),
        IndexColor(8f, Color(0xFFCE2421)),
        IndexColor(9f, Color(0xFF951111)),
        IndexColor(10f, Color(0xFF680A05))
    )
}

val LocalUVIndexGradientIndexColors = staticCompositionLocalOf {
    listOf(
        IndexColor(0f, Color(0xFF3E840D)),
        IndexColor(3f, Color(0xFFDCC635)),
        IndexColor(6f, Color(0xFFDC6F35)),
        IndexColor(9f, Color(0xFFDC3B35)),
        IndexColor(11f, Color(0xFFBD35DC))
    )
}

val LocalTemperatureGradientIndexColors = staticCompositionLocalOf {
    listOf(
        IndexColor(-40f, Color(0xFF511089)),
        IndexColor(-30f, Color(0xFF7015C0)),
        IndexColor(-20f, Color(0xFF4815C0)),
        IndexColor(-10f, Color(0xFF3652DC)),
        IndexColor(0f, Color(0xFF157CC0)),
        IndexColor(10f, Color(0xFF3CC4A2)),
        IndexColor(20f, Color(0xFF67A043)),
        IndexColor(30f, Color(0xFFDCB835)),
        IndexColor(40f, Color(0xFFDC5C35)),
        IndexColor(50f, Color(0xFFDC3535)),
    )
}

val LocalWeatherIconsColors = staticCompositionLocalOf {
    WeatherIconsColors(
        sun = Color(0xFFF8D74A),
        moon = Color(0xFFA8A439),
        star = Color(0xFFDEDC99),
        cloud = Color(0xFFFFFFFF),
        overcast = Color(0xFF97A2AC),
        rainDropCircle = Color(0xFFFFFFFF),
        rainDropWater = Color(0xFF4791C9),
        thunder = Color(0xFFF1C312)
    )
}

@Immutable
data class AirQualityColors(
    val good: Color,
    val moderate: Color,
    val unhealthySens: Color,
    val unhealthy: Color,
    val veryUnhealthy: Color,
    val hazardous: Color,
)

@Immutable
data class WeatherIconsColors(
    val sun: Color,
    val moon: Color,
    val star: Color,
    val cloud: Color,
    val overcast: Color,
    val rainDropCircle: Color,
    val rainDropWater: Color,
    val thunder: Color,
)

@Immutable
data class IndexColor(
    val level: Float,
    val color: Color,
)

object WeatherTheme {
    val airColors: AirQualityColors
        @Composable
        get() = LocalAirQualityColors.current
    val aqiGradientIndex: List<IndexColor>
        @Composable
        get() = LocalAqiGradientColors.current
    val uvIndexGradientIndex: List<IndexColor>
        @Composable
        get() = LocalUVIndexGradientIndexColors.current
    val temperatureGradientIndex: List<IndexColor>
        @Composable
        get() = LocalTemperatureGradientIndexColors.current
    val weatherIconsColors: WeatherIconsColors
        @Composable
        get() = LocalWeatherIconsColors.current
}