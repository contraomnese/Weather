package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.backgroundGradient

@Composable
fun AnimatedGradientBackgroundScaffold(
    snackBarHostState: SnackbarHostState,
    insets: WindowInsets? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Scaffold(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colorStops = MaterialTheme.colorScheme.backgroundGradient.toTypedArray(),
                    startY = offset,
                )
            ),
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
    val snackBarHostState = LocalSnackbarHostState.current
    WeatherTheme {
        AnimatedGradientBackgroundScaffold(
            snackBarHostState
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

            }
        }
    }
}