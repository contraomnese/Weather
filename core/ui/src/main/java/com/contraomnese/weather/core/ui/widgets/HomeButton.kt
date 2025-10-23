package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.itemWidth56

@Composable
fun HomeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier.width(itemWidth56),
        onClick = onClick
    ) {
        Icon(
            imageVector = WeatherIcons.Menu,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}