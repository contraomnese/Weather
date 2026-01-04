package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    val infiniteTransition = rememberInfiniteTransition(label = "background_pan_animation")

    val tx by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translation_x_value"
    )

    Image(
        painter = painterResource(backgroundResId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = 1.2f
                scaleY = 1.2f

                translationX = tx

                alpha = 0.45f
            },
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