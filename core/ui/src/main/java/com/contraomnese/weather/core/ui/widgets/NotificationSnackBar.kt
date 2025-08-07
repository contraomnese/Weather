package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16

@Composable
fun NotificationSnackBar(
    modifier: Modifier = Modifier,
    message: String,
) {

    Snackbar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(cornerRadius16),
        modifier = modifier
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = message,
                maxLines = 2,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
fun NotificationSnackBarPreview() {
    WeatherTheme {
        NotificationSnackBar(
            message = "Message",
        )
    }
}

@Preview
@Composable
fun NotificationSnackBarWithLongMessagePreview() {
    WeatherTheme {
        NotificationSnackBar(
            message = "Message long long long long long long long" +
                    "long long long long long long long long long long long long long long long" +
                    "long long long long long long long long long long long long long long long",
        )
    }
}