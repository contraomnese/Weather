package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemWidth56

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    var clicked by remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier.width(itemWidth56),
        enabled = !clicked,
        onClick = {
            onClick()
            clicked = true
        }
    ) {
        Icon(
            imageVector = if (clicked) WeatherIcons.RemoveFavorite else WeatherIcons.AddFavorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun FavoriteButtonPreview() {
    WeatherTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FavoriteButton(onClick = {})
        }

    }
}