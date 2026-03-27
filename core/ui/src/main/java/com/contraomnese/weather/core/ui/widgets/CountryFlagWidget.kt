package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.contraomnese.weather.design.theme.itemHeight20

@Composable
fun CountryFlagWidget(
    modifier: Modifier = Modifier,
    flagCode: String,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .size(itemHeight20)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            model = ImageRequest.Builder(context)
                .data("https://www.xe.com/svgs/flags/${flagCode.lowercase()}.static.svg")
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = "Flag by country code $flagCode",
            contentScale = ContentScale.Crop
        )
    }
}