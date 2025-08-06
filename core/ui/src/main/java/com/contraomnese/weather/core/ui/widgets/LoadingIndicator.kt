package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.design.DevicePreviews
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.itemWidth64

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    thickness: Dp = itemThickness2,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        trackColor = Color.Transparent,
        strokeWidth = thickness,
        strokeCap = StrokeCap.Square
    )
}

@DevicePreviews
@Composable
fun LoadingIndicatorPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Box(Modifier.fillMaxSize()) {
            LoadingIndicator(
                Modifier
                    .align(Alignment.Center)
                    .width(itemWidth64)
            )
        }
    }
}