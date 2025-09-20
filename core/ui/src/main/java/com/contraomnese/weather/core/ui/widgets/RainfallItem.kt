package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.Rainfall
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemHeight32
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import kotlin.random.Random

@Composable
fun RainfallItem(
    modifier: Modifier = Modifier,
    rainfallBeforeNow: List<Double>,
    rainfallAfterNow: List<Double>,
    rainfallNow: Double,
    maxRainfall: Double,
    isRainingExpected: Boolean = false,
    precipitationUnit: PrecipitationUnit,
) {
    var displayValue by remember { mutableFloatStateOf(rainfallNow.toFloat()) }

    val precipitationRes = remember(precipitationUnit) {
        when (precipitationUnit) {
            PrecipitationUnit.Millimeters -> R.string.rainfall_value_mm
            PrecipitationUnit.Inches -> R.string.rainfall_value_inch
        }
    }

    val rainFallBeforeNowBySixHours = remember(rainfallBeforeNow) {
        val cutoff = rainfallBeforeNow.size - rainfallBeforeNow.size % 6
        rainfallBeforeNow.takeLast(cutoff).chunked(6) { it.sum() }
    }

    val rainFallAfterNowByThreeHours = remember(rainfallAfterNow) {
        val cutoff = rainfallAfterNow.size - rainfallAfterNow.size % 3
        rainfallAfterNow.take(cutoff.coerceAtMost(12)).chunked(3) { it.sum() }
    }

    val maxRainfallByThreeHours = remember(maxRainfall) {
        (maxRainfall * 3).toFloat()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight160),
    ) {
        if (isRainingExpected) {
            Text(
                text = stringResource(R.string.expected_raining),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            modifier = Modifier.height(itemHeight32),
            text = AnnotatedString(stringResource(precipitationRes, displayValue)),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                lineBreak = LineBreak.Heading,
                textAlign = TextAlign.Center
            ),
            maxLines = 1
        )

        Row(
            modifier = Modifier.height(itemHeight140),
            horizontalArrangement = Arrangement.spacedBy(space8)
        ) {
            rainFallBeforeNowBySixHours.forEachIndexed { index, value ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Rainfall(
                        modifier = Modifier
                            .clickable {
                                displayValue = value.toFloat()
                            }
                            .fillMaxSize()
                            .weight(1f),
                        value = value.toFloat(),
                        maxValue = maxRainfallByThreeHours
                    )
                    Text(
                        text = "-${(rainFallBeforeNowBySixHours.size - index) * 6}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Rainfall(
                    modifier = Modifier
                        .clickable {
                            displayValue = rainfallNow.toFloat()
                        }
                        .fillMaxSize()
                        .weight(1f),
                    value = rainfallNow.toFloat(),
                    maxValue = maxRainfallByThreeHours
                )
                Text(
                    text = "0",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            rainFallAfterNowByThreeHours.forEachIndexed { index, value ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Rainfall(
                        modifier = Modifier
                            .clickable {
                                displayValue = value.toFloat()
                            }
                            .fillMaxSize()
                            .weight(1f),
                        value = value.toFloat(),
                        maxValue = maxRainfallByThreeHours
                    )
                    Text(
                        text = "+${(index + 1) * 3}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x79397E93, locale = "ru")
@Composable
private fun RainfallItemPreview() {
    WeatherTheme {
        RainfallItem(
            rainfallBeforeNow = List(12) { Random.nextDouble(0.3, 1.2) },
            rainfallAfterNow = List(12) { Random.nextDouble(0.1, 1.8) },
            rainfallNow = 2.2,
            isRainingExpected = false,
            maxRainfall = 2.5,
            precipitationUnit = PrecipitationUnit.Millimeters
        )
    }
}