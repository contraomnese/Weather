package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.contraomnese.weather.core.ui.utils.getResources
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition


@Composable
fun ImageBackgroundWithGradient(
    condition: WeatherCondition,
) {
    val isPreview = LocalInspectionMode.current
    val heightScreen = LocalConfiguration.current.screenHeightDp
    val density = LocalDensity.current
    if (isPreview) {
        Image(
            painter = painterResource(id = condition.getResources().backgroundResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    } else {
        SubcomposeAsyncImage(
            model = condition.getResources().backgroundResId,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = with(density) { heightScreen * -0.2f }
                },
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(condition.getResources().color)
                )
            },
        )
    }

    Box(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        condition.getResources().color,
                        condition.getResources().color
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
private fun ImageBackgroundWithClearPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.CLEAR)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithPartlyCloudyPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.PARTLY_CLOUDY)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithCloudyPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.CLOUDY)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithFogPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.FOG)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithFreezingFogPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.FREEZING_FOG)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithDrizzlePreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.DRIZZLE_MODERATE)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithFreezingDrizzlePreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.FREEZING_DRIZZLE_LIGHT)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithSnowFallPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.SNOW_FALL_MODERATE)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithRainPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.RAIN_MODERATE)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithRainShowersPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.RAIN_SHOWERS_SLIGHT)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageBackgroundWithThunderStormShowersPreview() {
    WeatherTheme {
        CompositionLocalProvider {
            ImageBackgroundWithGradient(condition = WeatherCondition.THUNDERSTORM)
        }
    }
}