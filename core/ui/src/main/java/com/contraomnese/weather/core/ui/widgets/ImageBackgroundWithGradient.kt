package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.contraomnese.weather.core.ui.composition.LocalWeatherBackgrounds
import com.contraomnese.weather.core.ui.composition.weatherBackgrounds
import com.contraomnese.weather.core.ui.utils.getBackground
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition


@Composable
fun ImageBackgroundWithGradient(
    condition: WeatherCondition,
) {
    SubcomposeAsyncImage(
        model = condition.getBackground().resId,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = -600f
            },
        alpha = 0.5f,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(condition.getBackground().color)
            )
        },
    )

    Box(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        condition.getBackground().color,
                        condition.getBackground().color
                    ),
                    startY = with(LocalDensity.current) { (400.dp).toPx() },
                    endY = Float.POSITIVE_INFINITY
                ),
            )
            .fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithGradientPreview() {
    WeatherTheme {
        CompositionLocalProvider(LocalWeatherBackgrounds provides weatherBackgrounds) {
            ImageBackgroundWithGradient(condition = WeatherCondition.CLEAR)
        }
    }
}