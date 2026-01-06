package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding32
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastHour
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlin.random.Random


@Composable
fun ForecastHourlyLazyRow(
    modifier: Modifier = Modifier,
    items: List<ForecastHour>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(padding16)
    ) {
        LazyRow(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding32)
        ) {
            itemsIndexed(items) { index, it ->
                ForecastHourlyItem(
                    time = it.time,
                    condition = it.condition,
                    temperature = it.temperature,
                    isNow = index == 0,
                    isDay = it.isDay
                )
            }
        }
    }
}

@Preview
@Composable
private fun ForecastHourlyLazyRowPreview() {
    WeatherTheme {

        ForecastHourlyLazyRow(
            items = List(9) { index ->
                ForecastHour(
                    time = "0${index}:00",
                    condition = WeatherCondition.CLEAR,
                    temperature = "${index + index}",
                    isDay = Random.nextBoolean()
                )
            }
        )
    }
}