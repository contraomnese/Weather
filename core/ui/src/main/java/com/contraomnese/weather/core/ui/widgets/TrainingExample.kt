package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.contraomnese.weather.design.theme.itemHeight64

@Composable
fun TrainingExample(
    modifier: Modifier = Modifier,
    code: Int = 1087,
) {

    val context = LocalContext.current

    var extractedColor by remember { mutableStateOf(Color.Black) }

    val drawableBackgroundRes = when (code) {
        1000 -> R.drawable.sun_large

        1003 -> R.drawable.partly_cloud

        1006, 1009 -> R.drawable.overcast

        1030, 1135, 1147 -> R.drawable.fog

        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195,
        1240, 1243, 1246, 1273, 1276,
            -> R.drawable.rain

        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225,
        1255, 1258, 1279, 1282,
            -> R.drawable.snow

        1087 -> R.drawable.thunder

        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207,
        1237, 1249, 1252, 1261, 1264,
            -> R.drawable.sleet

        else -> R.drawable.moderate
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

        LoadingIndicator(
            modifier = Modifier
                .height(itemHeight64)
                .align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TrainingExamplePreview() {
    WeatherTheme {
        TrainingExample()
    }
}