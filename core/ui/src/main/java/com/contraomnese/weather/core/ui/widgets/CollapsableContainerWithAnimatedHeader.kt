package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.itemHeight300
import com.contraomnese.weather.design.theme.itemHeight32
import com.contraomnese.weather.design.theme.itemThickness1
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8

@Composable
fun CollapsableContainerWithAnimatedHeader(
    modifier: Modifier = Modifier,
    minHeaderHeight: Dp = itemHeight32,
    headerTitle: String = "Header",
    headerIcon: ImageVector = WeatherIcons.Default,
    alertTitle: String? = null,
    currentBodyHeight: Float?,
    progress: Float,
    onContentMeasured: (Int) -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {

    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val textStyleHeader = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp)

    var isHeaderTitleExpanded by remember { mutableStateOf(false) }
    var headerTitleWidth by remember(isHeaderTitleExpanded) { mutableIntStateOf(0) }
    val headerTitleMaxLines = remember(isHeaderTitleExpanded) { if (isHeaderTitleExpanded) Int.MAX_VALUE else 3 }
    var fadeHeight by remember { mutableFloatStateOf(0f) }

    val maxHeaderHeight = remember(alertTitle, headerTitleMaxLines, headerTitleWidth) {
        if (alertTitle != null) {
            val result = textMeasurer.measure(
                text = alertTitle,
                style = textStyleHeader,
                constraints = Constraints.fixedWidth(headerTitleWidth),
                maxLines = headerTitleMaxLines,
            )
            with(density) { result.size.height.toDp() }
        } else {
            minHeaderHeight
        }
    }

    val headerHeight by remember(progress, alertTitle, maxHeaderHeight) {
        derivedStateOf {
            if (alertTitle == null) {
                minHeaderHeight
            } else {
                if (maxHeaderHeight > minHeaderHeight) lerp(minHeaderHeight, maxHeaderHeight, progress)
                else minHeaderHeight
            }
        }
    }

    Column(
        modifier = modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius16))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding16, vertical = padding4)
                .onGloballyPositioned {
                    headerTitleWidth = it.size.width
                }
                .height(headerHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            if (alertTitle != null) {
                Text(
                    text = alertTitle,
                    style = textStyleHeader,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = headerTitleMaxLines,
                    onTextLayout = {
                        fadeHeight = if (it.hasVisualOverflow) with(density) { textStyleHeader.fontSize.toPx() * 2 } else 0f
                    },
                    modifier = Modifier
                        .clickable(
                            enabled = fadeHeight != 0f || isHeaderTitleExpanded,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            isHeaderTitleExpanded = !isHeaderTitleExpanded
                        }
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                            alpha = if (progress >= 0.15f) progress else 0f
                            translationY = -(1f - progress) * 5f
                        }
                        .drawWithContent {
                            drawContent()

                            val heightPx = size.height
                            if (heightPx > 0f && fadeHeight > 0f) {
                                val gradient = Brush.verticalGradient(
                                    0f to Color.Black,
                                    1f to Color.Transparent,
                                    startY = heightPx - fadeHeight,
                                    endY = heightPx
                                )
                                drawRect(
                                    brush = gradient,
                                    size = size,
                                    blendMode = BlendMode.DstIn
                                )
                            }
                        }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(padding8),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer {
                    alpha = if (alertTitle == null) 1f else if (progress < 0.15f) 1f - progress else 0f
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
        HorizontalDivider(
            modifier = Modifier.graphicsLayer {
                alpha = if (progress <= 0.15f) 0f else 1f
            },
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = itemThickness1,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onContentMeasured(coordinates.size.height)
                }
                .clipToBounds()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val visibleHeight = currentBodyHeight?.toInt() ?: placeable.height

                    val offsetY = (placeable.height - visibleHeight).coerceAtLeast(0)
                    layout(constraints.maxWidth, visibleHeight) {
                        placeable.place(0, -offsetY)
                    }
                },
            contentAlignment = Alignment.TopCenter,
            content = content
        )
    }
}

@Composable
@Preview
private fun CollapsableContainerWithAnimatedHeaderPreview() {
    WeatherTheme {
        CollapsableContainerWithAnimatedHeader(
            currentBodyHeight = 400f,
            alertTitle = "Днём 31 октября на территории области ожидается дождь, местами сильный, местами сильный.",
            progress = 1f
        ) {
            Box(
                modifier = Modifier.height(itemHeight300),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Preview"
                )
            }
        }
    }
}

@Composable
@Preview
private fun CollapsableContainerWithAnimatedHeaderEmptyAlertTitlePreview() {
    WeatherTheme {
        CollapsableContainerWithAnimatedHeader(
            currentBodyHeight = 400f,
            alertTitle = null,
            progress = 1f
        ) { }
    }
}