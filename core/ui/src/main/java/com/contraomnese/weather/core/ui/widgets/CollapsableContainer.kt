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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8

@Composable
fun CollapsableContainer(
    modifier: Modifier = Modifier,
    headerHeight: Dp,
    currentHeight: Float,
    maxHeight: Float,
    headerTitle: String = "Header",
    headerIcon: ImageVector = WeatherIcons.Default,
    content: @Composable ColumnScope.() -> Unit,
) {
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
                .height(headerHeight)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(padding8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = null,
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
                    layout(constraints.maxWidth, currentHeight.toInt()) {
                        val y = (maxHeight - currentHeight).toInt()
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