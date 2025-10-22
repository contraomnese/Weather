package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.padding8

@Composable
fun AnimatedIcon(
    visible: Boolean,
    onClick: () -> Unit,
    animationDuration: Int = 500,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandHorizontally(animationSpec = tween(durationMillis = animationDuration)),
        exit = shrinkHorizontally(animationSpec = tween(durationMillis = animationDuration))
    ) {
        Icon(
            modifier = Modifier
                .padding(start = padding8)
                .clickable { onClick() },
            imageVector = WeatherIcons.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}