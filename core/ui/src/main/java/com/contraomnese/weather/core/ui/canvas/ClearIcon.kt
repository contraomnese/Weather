package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun ClearIcon(
    modifier: Modifier = Modifier,
    isNight: Boolean,
) {
    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        if (isNight) {
            MoonIcon(
                modifier = Modifier.fillMaxSize(0.75f)
            )
        } else {
            SunIcon(
                modifier = Modifier.fillMaxSize(0.75f)
            )
        }
    }
}

@Preview
@Composable
fun ClearSunIconPreview() {
    WeatherTheme {
        ClearIcon(
            modifier = Modifier
                .size(200.dp),
            isNight = false
        )
    }
}

@Preview
@Composable
fun ClearNightIconPreview() {
    WeatherTheme {
        ClearIcon(
            modifier = Modifier
                .size(200.dp),
            isNight = true
        )
    }
}