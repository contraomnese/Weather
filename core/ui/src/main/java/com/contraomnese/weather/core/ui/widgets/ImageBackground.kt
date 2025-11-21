package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight160

@Composable
fun ImageBackground(
    modifier: Modifier = Modifier,
    @DrawableRes backgroundResId: Int,
) {

    Image(
        painter = painterResource(backgroundResId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 0.45f },
    )
}


@Preview(showBackground = true)
@Composable
private fun ImageBackgroundPreview() {
    WeatherTheme {
        ImageBackground(
            modifier = Modifier
                .height(itemHeight160)
                .fillMaxWidth(),
            backgroundResId = R.drawable.clear
        )
    }
}