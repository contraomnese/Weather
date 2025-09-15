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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding8

@Composable
fun TitleSection(
    height: Float,
    location: String,
    currentTemp: String,
    maxTemp: String,
    minTemp: String,
    condition: String,
) {

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { height.toDp() })
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
                    stringResource(R.string.max_min_temperature_title, maxTemp, minTemp),
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

@Preview(locale = "ru")
@Composable
fun TitleSectionPreview() {
    WeatherTheme {
        TitleSection(
            height = 400f,
            location = "pertinax",
            currentTemp = "23",
            maxTemp = "26",
            minTemp = "20",
            condition = "Clear"
        )
    }
}