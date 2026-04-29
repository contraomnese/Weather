package com.contraomnese.weather.design.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val AirGoodColor = Color(0xFFFFFFFF)
internal val AirModerateColor = Color(0xFFDCD807)
internal val AirUnhealthyForSensGroupsColor = Color(0xFFf0a642)
internal val AirUnhealthyColor = Color(0xFFff4d50)
internal val AirVeryUnhealthyColor = Color(0xFF960132)
internal val AirHazardousColor = Color(0xFF7d2181)

val ColorScheme.backgroundGradient: List<Pair<Float, Color>>
    @Composable
    get() = listOf(
        0.00f to Color(0xFF2E83FA),
        0.15f to Color(0xFF2A7AEC),
        0.35f to Color(0xFF2673E2),
        0.55f to Color(0xFF2169D0),
        0.75f to Color(0xFF1C5DBB),
        1.00f to Color(0xFF1955AC)
    )


val ColorScheme.emojiColors: List<Color>
    @Composable
    get() = listOf(Color(0xFFFFD93B), Color(0xFFEFAC40), Color(0xFFDEB435))

val WeatherConditionsClear = Color(0xFF4F372A)
val WeatherConditionsPartlyCloudy = Color(0xFFBEC2C5)
val WeatherConditionsCloudy = Color(0xFF9D9EA0)
val WeatherConditionsFog = Color(0xFF565656)
val WeatherConditionsFreezingFog = Color(0xFFD3E5EF)
val WeatherConditionsDrizzle = Color(0xFF2E362D)
val WeatherConditionsFreezingDrizzle = Color(0xFF435D65)
val WeatherConditionsSnowFall = Color(0xFFD3E5EF)
val WeatherConditionsRain = Color(0xFF2E362D)
val WeatherConditionsRainShowers = Color(0xFF2E362D)
val WeatherConditionsThunderStorm = Color(0xFF002E5F)