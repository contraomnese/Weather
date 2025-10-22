package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.TemperatureRangeLine
import com.contraomnese.weather.core.ui.icons.ConditionsIcon
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight26
import com.contraomnese.weather.design.theme.itemHeight6
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.itemWidth40
import com.contraomnese.weather.design.theme.padding12
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.CompactWeatherCondition

@Composable
fun ForecastDailyItem(
    modifier: Modifier = Modifier,
    dayNumber: String,
    dayName: String,
    condition: CompactWeatherCondition,
    minTemperature: Int,
    maxTemperature: Int,
    maxRangeTemperature: Int,
    minRangeTemperature: Int,
    currentTemperature: Int? = null,
    temperatureUnit: TemperatureUnit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight64),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding12),
    ) {

        Text(
            modifier = Modifier.width(60.dp),
            text = dayNumber,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = dayName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        ConditionsIcon(condition = condition, modifier = Modifier.size(itemWidth40))
        Text(
            modifier = Modifier.height(itemHeight26),
            text = stringResource(R.string.current_temperature_title, minTemperature.toString()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        TemperatureRangeLine(
            minRange = minRangeTemperature.toFloat(),
            maxRange = maxRangeTemperature.toFloat(),
            min = minTemperature.toFloat(),
            current = currentTemperature?.toFloat(),
            max = maxTemperature.toFloat(),
            temperatureUnit = temperatureUnit,
            modifier = Modifier
                .weight(1f)
                .height(itemHeight6),
        )
        Text(
            modifier = Modifier.height(itemHeight26),
            text = stringResource(R.string.current_temperature_title, maxTemperature.toString()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", backgroundColor = 0xFF1C232B, showSystemUi = false)
@Composable
private fun ForecastDailyItemPreview() {
    WeatherTheme {
        ForecastDailyItem(
            dayNumber = "22.06",
            dayName = "Чт",
            condition = CompactWeatherCondition.CLEAR,
            minTemperature = 19,
            maxTemperature = 23,
            maxRangeTemperature = 25,
            minRangeTemperature = 17,
            currentTemperature = 21,
            temperatureUnit = TemperatureUnit.Celsius
        )
    }
}