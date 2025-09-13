package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.DewPoint
import com.contraomnese.weather.core.ui.canvas.DewPointFractions
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.TemperatureUnit

@Composable
fun HumidityItem(
    modifier: Modifier = Modifier,
    humidity: Int,
    dewPoint: Int,
    temperatureUnit: TemperatureUnit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight160),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(padding8),
        ) {
            Text(
                text = stringResource(R.string.humidity_value, humidity),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider()
            Text(
                text = stringResource(R.string.dew_point, dewPoint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        DewPoint(
            modifier = Modifier
                .height(itemHeight160)
                .weight(1f),
            dewPointFraction = when (temperatureUnit) {
                TemperatureUnit.Celsius -> DewPointFractions.fromC(dewPoint)
                TemperatureUnit.Fahrenheit -> DewPointFractions.fromF(dewPoint)
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x79397E93)
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