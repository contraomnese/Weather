package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight26
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.padding8

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    location: String,
    currentTemp: String,
    maxTemp: String,
    minTemp: String,
    feelsLikeTemp: String,
    condition: String,
) {

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = padding8)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = .0f)),
        contentAlignment = Alignment.Center
    ) {
        if (maxHeight > 250.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(padding8)
            ) {
                Text(
                    location, style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.current_temperature_title, currentTemp),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = condition,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    stringResource(R.string.feels_like_temperature_title, feelsLikeTemp, maxTemp, minTemp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(padding8)
            ) {
                Text(
                    location, style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(padding8)
                ) {
                    Text(
                        text = stringResource(R.string.temperature, currentTemp),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            lineBreak = LineBreak.Simple
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    VerticalDivider(
                        modifier = Modifier.height(itemHeight26),
                        thickness = itemThickness2,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = condition,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TitleSectionFullPreview() {
    WeatherTheme {
        TitleSection(
            modifier = Modifier.height(400.dp),
            location = "Moscow",
            currentTemp = "23",
            feelsLikeTemp = "26",
            maxTemp = "26",
            minTemp = "20",
            condition = "Clear"
        )
    }
}

@Preview
@Composable
private fun TitleSectionPreview() {
    WeatherTheme {
        TitleSection(
            modifier = Modifier.height(200.dp),
            location = "Moscow",
            currentTemp = "23",
            feelsLikeTemp = "26",
            maxTemp = "26",
            minTemp = "20",
            condition = "Clear"
        )
    }
}