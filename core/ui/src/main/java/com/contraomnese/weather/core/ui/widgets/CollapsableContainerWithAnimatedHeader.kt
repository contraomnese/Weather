package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding32
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8

@Composable
fun CollapsableContainerWithAnimatedHeader(
    modifier: Modifier = Modifier,
    minHeaderHeight: Dp,
    headerTitle: String = "Header",
    headerIcon: ImageVector = WeatherIcons.Default,
    alertTitle: String? = null,
    currentBodyHeight: Float,
    maxBodyHeight: Float,
    progress: Float,
    content: @Composable ColumnScope.() -> Unit,
) {

    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val textStyleHeader = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val maxHeaderHeight = remember(alertTitle) {
        if (alertTitle != null) {
            val result = textMeasurer.measure(
                text = alertTitle,
                style = textStyleHeader,
                constraints = Constraints.fixedWidth(with(density) { screenWidth.toPx().toInt() })
            )
            with(density) { result.size.height.toDp() } + padding32
        } else {
            minHeaderHeight
        }
    }

    val headerHeight by remember(progress, alertTitle) {
        derivedStateOf {
            if (alertTitle == null) {
                minHeaderHeight
            } else {
                lerp(minHeaderHeight, maxHeaderHeight, progress)
            }
        }
    }

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius16))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding16, vertical = padding4)
                .height(headerHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            if (alertTitle != null) {
                Text(
                    text = alertTitle,
                    style = textStyleHeader,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.graphicsLayer {
                        alpha = progress
                        translationY = -(1f - progress) * 5f
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(padding8),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer {
                    alpha = if (alertTitle == null) 1f else if (progress < 0.35f) 1f - progress else 0f
                }
            ) {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                )
                Text(
                    text = headerTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, currentBodyHeight.toInt()) {
                        val y = (maxBodyHeight - currentBodyHeight).toInt()
                        placeable.place(0, -y)
                    }
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(padding8),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Composable
@Preview
private fun CollapsableContainerWithAnimatedHeaderPreview() {
    WeatherTheme {
        CollapsableContainerWithAnimatedHeader(
            minHeaderHeight = 46.dp,
            currentBodyHeight = 0f,
            alertTitle = "Alert",
            maxBodyHeight = 650f,
            progress = 1f
        ) { }
    }
}

@Composable
@Preview
private fun CollapsableContainerWithAnimatedHeaderEmptyAlertTitlePreview() {
    WeatherTheme {
        CollapsableContainerWithAnimatedHeader(
            minHeaderHeight = 46.dp,
            currentBodyHeight = 0f,
            alertTitle = null,
            maxBodyHeight = 650f,
            progress = 1f
        ) { }
    }
}