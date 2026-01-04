package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun AnimatedGradientBackground(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3989BF),
                        Color(0xFF317FB3),
                        Color(0xFF2C72A1),
                        Color(0xFF266289),
                        Color(0xFF1D4C6A)
                    ),
                    startY = offset
                )
            )
    ) {
        content()
    }
}

@Preview(showBackground = false, showSystemUi = false, device = "id:pixel_5")
@Composable
private fun AGBPreview() {
    WeatherTheme {
        AnimatedGradientBackground() {
            Box(modifier = Modifier.fillMaxSize()) {

            }
        }
    }
}