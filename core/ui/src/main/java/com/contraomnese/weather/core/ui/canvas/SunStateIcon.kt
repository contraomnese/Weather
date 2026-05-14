package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun SunStateIcon(
    modifier: Modifier = Modifier,
    sunset: Boolean = false,
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.TopCenter,
    ) {

        val sunsetModifier = if (sunset) Modifier.rotate(180f) else Modifier
        val side = min(this.maxWidth, this.maxHeight)

        SunIcon(
            modifier = Modifier
                .fillMaxSize(0.95f)
                .offset(
                    y = side * .28f
                )
                .drawWithContent {
                    clipRect(
                        top = 0f,
                        left = 0f,
                        right = size.width,
                        bottom = size.height * 0.55f
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
            angle = 0f
        )
        ArrowIcon(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .then(sunsetModifier)
        )

        LineIcon(
            modifier = Modifier
                .fillMaxWidth()
                .height(side * 0.5f)
                .offset(
                    y = side * 0.45f
                )
        )
    }
}

@Preview
@Composable
fun SunriseIconPreview() {
    WeatherTheme {
        SunStateIcon(
            modifier = Modifier
                .size(200.dp),
        )
    }
}

@Preview
@Composable
fun SunsetIconPreview() {
    WeatherTheme {
        SunStateIcon(
            modifier = Modifier
                .size(200.dp),
            sunset = true
        )
    }
}