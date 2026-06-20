package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding12

@Composable
fun PreferencesSwitcher(
    enabled: Boolean = false,
    onEnabled: (Boolean) -> Unit,
    @StringRes title: Int,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = padding12)
    ) {
        Text(
            stringResource(title),
            modifier = Modifier.fillMaxWidth(0.8f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            maxLines = 2,
            softWrap = true
        )
        Switch(
            checked = enabled,
            onCheckedChange = onEnabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onSecondary,
                checkedTrackColor = MaterialTheme.colorScheme.secondary,
                uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun PreferencesSwitcherPreview() {
    WeatherTheme {
        PreferencesSwitcher(
            title = R.string.forecast_auto_sync_enable,
            enabled = true,
            onEnabled = { }
        )
    }
}