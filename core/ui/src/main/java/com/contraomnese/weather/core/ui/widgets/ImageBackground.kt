package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.contraomnese.weather.core.ui.utils.extractBottomColor
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition

@Composable
fun ImageBackground(
    modifier: Modifier = Modifier,
    condition: CompactWeatherCondition,
) {

    val context = LocalContext.current

    var extractedColor by remember { mutableStateOf(Color.Black) }

    val drawableBackgroundRes = when (condition) {
        CompactWeatherCondition.CLEAR -> R.drawable.sun_large
        CompactWeatherCondition.PARTLY_CLOUDY -> R.drawable.partly_cloud
        CompactWeatherCondition.CLOUDY -> R.drawable.overcast
        CompactWeatherCondition.FOG -> R.drawable.fog
        CompactWeatherCondition.RAIN -> R.drawable.rain
        CompactWeatherCondition.SNOW -> R.drawable.snow
        CompactWeatherCondition.THUNDER -> R.drawable.thunder
        CompactWeatherCondition.SLEET -> R.drawable.sleet
    }

    LaunchedEffect(drawableBackgroundRes) {
        val bitmap = ResourcesCompat.getDrawable(context.resources, drawableBackgroundRes, context.theme)?.current?.toBitmap()
        if (bitmap != null) {
            extractedColor = extractBottomColor(bitmap)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = drawableBackgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = -600f
                },
        )

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            extractedColor,
                            extractedColor
                        ),
                        startY = with(LocalDensity.current) { (400.dp).toPx() },
                        endY = Float.POSITIVE_INFINITY
                    ),
                )
                .fillMaxSize()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TrainingExamplePreview() {
    WeatherTheme {
        ImageBackground(condition = CompactWeatherCondition.CLEAR)
    }
}