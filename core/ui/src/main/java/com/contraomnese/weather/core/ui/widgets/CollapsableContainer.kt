package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8

@Composable
fun CollapsableContainer(
    modifier: Modifier = Modifier,
    headerHeight: Dp,
    currentBodyHeight: Float?,
    headerTitle: String = "Header",
    headerIcon: ImageVector = WeatherIcons.Default,
    onContentMeasured: (Int) -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius16))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = padding4, horizontal = padding16)
                .height(headerHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(padding8),
                verticalAlignment = Alignment.CenterVertically
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
                    onContentMeasured(placeable.height)
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

@Preview(showBackground = true)
@Composable
fun CollapsableContainerPreview() {
    MaterialTheme {
        CollapsableContainer(
            headerHeight = 46.dp,
            currentBodyHeight = 350f,
            headerTitle = "Daily Forecast",
            headerIcon = WeatherIcons.Daily
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                repeat(3) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Row $index",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

