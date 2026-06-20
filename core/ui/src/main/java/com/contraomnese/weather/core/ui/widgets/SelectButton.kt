package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.contraomnese.weather.design.theme.padding12
import com.contraomnese.weather.design.theme.padding8

@Composable
fun SelectButton(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    isConfirm: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .background(
                if (isConfirm) MaterialTheme.colorScheme.secondary
                else Color.Transparent
            )
            .padding(horizontal = padding12, vertical = padding8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isConfirm) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isConfirm) MaterialTheme.colorScheme.onSecondary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}