package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemHeight48
import com.contraomnese.weather.design.theme.padding4

@Composable
fun NotificationSnackBar(
    modifier: Modifier = Modifier,
    message: String,
) {

    Snackbar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(cornerRadius16),
        modifier = modifier
            .height(itemHeight48)
            .alpha(0.8f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = WeatherIcons.Notification,
                contentDescription = "Notification",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(itemHeight20)
            )
            Spacer(modifier = Modifier.width(padding4))
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = message,
                maxLines = 2,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis
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
                    "long long long long long long long long long long long long long long long",
        )
    }
}