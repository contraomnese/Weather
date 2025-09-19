package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.DewPointEmoji
import com.contraomnese.weather.core.ui.canvas.DewPointState
import com.contraomnese.weather.core.ui.canvas.HumidityLevel
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemHeight48
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.app.model.TemperatureUnit

@Composable
fun HumidityItem(
    modifier: Modifier = Modifier,
    humidity: Int,
    dewPoint: Int,
    temperatureUnit: TemperatureUnit,
) {

    val dewPointState = remember(dewPoint, temperatureUnit) {
        when (temperatureUnit) {
            TemperatureUnit.Celsius -> DewPointState.fromDewPointC(dewPoint)
            TemperatureUnit.Fahrenheit -> DewPointState.fromDewPointF(dewPoint)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight160),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space8)
    ) {

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1.5f),
            verticalArrangement = Arrangement.spacedBy(padding8),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DewPointEmoji(
                    modifier = Modifier.size(itemHeight48), state = dewPointState
                )
                Text(
                    text = when (dewPointState) {
                        DewPointState.Happy -> stringResource(R.string.dew_point_happy)
                        DewPointState.Neutral -> stringResource(R.string.dew_point_neutral)
                        DewPointState.Sad -> stringResource(R.string.dew_point_sad)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = stringResource(R.string.dew_point, dewPoint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        HumidityLevel(
            modifier = Modifier
                .height(itemHeight160)
                .weight(1f),
            value = humidity
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x79397E93, locale = "ru")
@Composable
private fun HumidityItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        HumidityItem(
            modifier = modifier,
            humidity = 45,
            dewPoint = 16,
            temperatureUnit = TemperatureUnit.Celsius
        )
    }
}