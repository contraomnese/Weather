package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition

@Composable
fun ImageBackground(
    modifier: Modifier = Modifier,
    condition: CompactWeatherCondition,
) {

    val drawableBackgroundRes = when (condition) {
        CompactWeatherCondition.CLEAR -> R.drawable.clear
        CompactWeatherCondition.PARTLY_CLOUDY -> R.drawable.partly_cloud
        CompactWeatherCondition.CLOUDY -> R.drawable.overcast
        CompactWeatherCondition.FOG -> R.drawable.fog
        CompactWeatherCondition.RAIN -> R.drawable.rain
        CompactWeatherCondition.SNOW -> R.drawable.snow
        CompactWeatherCondition.THUNDER -> R.drawable.thunder
        CompactWeatherCondition.SLEET -> R.drawable.sleet
    }

    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = drawableBackgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.5f,
            modifier = Modifier.fillMaxSize(),
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ImageBackgroundPreview() {
    WeatherTheme {
        ImageBackground(modifier = Modifier
            .height(itemHeight160)
            .fillMaxWidth(), condition = CompactWeatherCondition.CLEAR)
    }
}