package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.contraomnese.weather.core.ui.utils.toTextRes
import com.contraomnese.weather.design.theme.padding12
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space4

@Composable
inline fun <reified T : Enum<T>> RadioGroup(
    title: String,
    options: List<T>,
    selected: T,
    crossinline onSelected: (T) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = padding12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                softWrap = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = padding16)
            )

            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        shape = CircleShape
                    )
                    .padding(padding4),
                horizontalArrangement = Arrangement.spacedBy(space4)
            ) {
                options.forEach { option ->
                    val isSelected = option == selected
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onSelected(option) }
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.secondary
                                else Color.Transparent
                            )
                            .padding(horizontal = padding12, vertical = padding8),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(option.toTextRes()),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}