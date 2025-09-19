package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.icons.ConditionsIcon
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemHeight26
import com.contraomnese.weather.design.theme.itemWidth40
import com.contraomnese.weather.design.theme.padding16

@Composable
fun ForecastHourlyItem(
    modifier: Modifier = Modifier,
    time: String,
    conditionCode: Int,
    temperature: String,
    isNow: Boolean = false,
    isDay: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(padding16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.height(itemHeight20),
            text = if (isNow) stringResource(R.string.now) else time.substring(0, 2),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        ConditionsIcon(code = conditionCode, isNight = !isDay, modifier = Modifier.size(itemWidth40))
        Text(
            modifier = Modifier.height(itemHeight26),
            text = stringResource(R.string.current_temperature_title, temperature),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun ForecastHourlyItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        ForecastHourlyItem(
            time = "02:00",
            conditionCode = 1063,
            temperature = "19"
        )
    }
}

@Preview
@Composable
private fun ForecastHourlyItemNowPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        ForecastHourlyItem(
            time = "03:00",
            conditionCode = 1063,
            temperature = "19",
            isNow = true
        )
    }
}