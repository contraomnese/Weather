package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.itemHeight64

@Composable
fun AnimatedCircleButton(
    visible: Boolean,
    onClick: (Boolean) -> Unit,
    animationDuration: Int = 500,
) {
    AnimatedVisibility(
        visible = !visible,
        enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)),
        exit = fadeOut(animationSpec = tween(durationMillis = animationDuration))
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val pressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(if (pressed) 0.85f else 1f)

        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                modifier = Modifier
                    .size(itemHeight64)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(
                            bounded = true,
                            radius = itemHeight64,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 1f)
                        )
                    ) { }
                    .align(Alignment.Center),
                onClick = { onClick(true) },
                interactionSource = interactionSource,
            ) {
                Icon(
                    imageVector = WeatherIcons.GPS,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}