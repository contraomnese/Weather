package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.Wind
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8


@Composable
fun WindItem(
    modifier: Modifier = Modifier,
    windSpeed: String,
    gustSpeed: String,
    degree: Int,
    direction: String,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight160),
        horizontalArrangement = Arrangement.spacedBy(padding16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Wind(
            modifier = Modifier.size(itemHeight160),
            degree = degree,
            direction = direction
        )
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(padding8),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(padding8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = windSpeed,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column {
                    Text(
                        text = stringResource(R.string.units_meter_in_second),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = stringResource(R.string.wind_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.spacedBy(padding8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = gustSpeed,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column {
                    Text(
                        text = stringResource(R.string.units_meter_in_second),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = stringResource(R.string.gust_of_wind_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x79397E93)
@Composable
private fun WindItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        WindItem(
            modifier = modifier, windSpeed = "5", gustSpeed = "13", degree = 34, direction = "NW",

            )
    }
}