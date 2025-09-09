package com.contraomnese.weather.design.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val weatherScheme = lightColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    background = Color(0xFF4B6C95),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF2D3340),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFFFFFFF),
    secondary = Color(0xFF81CFFA),
    onSecondary = Color(0xFF000000),
    error = Color(0xFFFF453A),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> weatherScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = weatherTypography,
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S