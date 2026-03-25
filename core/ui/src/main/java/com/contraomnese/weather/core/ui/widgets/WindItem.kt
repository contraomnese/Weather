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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.contraomnese.weather.domain.app.model.WindSpeedUnit

@Composable
fun WindItem(
    modifier: Modifier = Modifier,
    windSpeed: String,
    gustSpeed: String,
    degrees: Int,
    windSpeedUnit: WindSpeedUnit,
) {

    val windSpeedUnitTitle = when (windSpeedUnit) {
        WindSpeedUnit.Kph -> R.string.units_killometer_per_hour
        WindSpeedUnit.Mph -> R.string.units_mile_per_hour
        WindSpeedUnit.Ms -> R.string.units_meter_in_second
    }

    val directionIndex by remember {
        derivedStateOf {
            (((degrees % 360 + 360) % 360 + 11.25) / 22.5).toInt() % 16
        }
    }

    val direction = when (directionIndex) {
        0 -> R.string.direction_n
        1 -> R.string.direction_nne
        2 -> R.string.direction_ne
        3 -> R.string.direction_ene
        4 -> R.string.direction_e
        5 -> R.string.direction_ese
        6 -> R.string.direction_se
        7 -> R.string.direction_sse
        8 -> R.string.direction_s
        9 -> R.string.direction_ssw
        10 -> R.string.direction_sw
        11 -> R.string.direction_wsw
        12 -> R.string.direction_w
        13 -> R.string.direction_wnw
        14 -> R.string.direction_nw
        15 -> R.string.direction_nnw
        else -> R.string.direction_n
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight160),
        horizontalArrangement = Arrangement.spacedBy(padding16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Wind(
            modifier = Modifier.size(itemHeight160),
            degree = degrees,
            direction = stringResource(direction)
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
                        text = stringResource(windSpeedUnitTitle),
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
                        text = stringResource(windSpeedUnitTitle),
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
            modifier = modifier,
            windSpeed = "5",
            gustSpeed = "13",
            degrees = 34,
            windSpeedUnit = WindSpeedUnit.Ms
        )
    }
}