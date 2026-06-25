package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun AnimatedGradientBackgroundScaffold(
    snackBarHostState: SnackbarHostState,
    insets: WindowInsets? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradientTransition")

    val animatedColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.secondary,
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AnimatedGradientBackground"
    )

    val backgroundColor = MaterialTheme.colorScheme.background

    Scaffold(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(animatedColor, backgroundColor)
                    )
                )
            },
        snackbarHost = { WeatherSnackBarHost(snackBarHostState) },
        containerColor = Color.Transparent,
        contentWindowInsets = insets ?: ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}

@Preview(showBackground = false, showSystemUi = false, device = "id:pixel_5")
@Composable
private fun AGBPreview() {
    val snackBarHostState = SnackbarHostState()
    WeatherTheme {
        AnimatedGradientBackgroundScaffold(
            snackBarHostState
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

            }
        }
    }
}