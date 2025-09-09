package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.Rainfall
import com.contraomnese.weather.core.ui.canvas.RainfallFractions
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space8
import kotlin.math.roundToInt

@Composable
fun RainfallItem(
    modifier: Modifier = Modifier,
    last24hoursAmount: Double,
    next24hoursAmount: Double,
    nextOneHourAmount: Double,
    isRainingExpected: Boolean = false,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight140),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Rainfall(
            modifier = Modifier
                .height(itemHeight140)
                .weight(1f),
            rainfallFraction = RainfallFractions.from(next24hoursAmount.roundToInt())
        )

        if (isRainingExpected) ExpectedRaining(last24hoursAmount, next24hoursAmount, nextOneHourAmount)
        else
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f),
                verticalArrangement = Arrangement.spacedBy(padding4),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.rainfall_value, last24hoursAmount),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.in_last_24_hours),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                HorizontalDivider()
                Column {
                    Text(
                        text = stringResource(R.string.rainfall_value, next24hoursAmount),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.expected_in_next_24_hours),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
    }
}

@Composable
private fun RowScope.ExpectedRaining(
    last24hoursAmount: Double,
    next24hoursAmount: Double,
    nextOneHourAmount: Double,
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .weight(3f)
            .padding(vertical = padding4),
        verticalArrangement = Arrangement.spacedBy(padding8),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.in_last_24_hours),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.rainfall_value, last24hoursAmount),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(R.string.rainfall_value, next24hoursAmount),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.expected_in_next_24_hours),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        HorizontalDivider()

        Text(
            text = stringResource(R.string.expected_raining),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = space8)
        ) {
            Text(
                text = stringResource(R.string.rainfall_value, nextOneHourAmount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.in_next_1_hour, next24hoursAmount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0x79397E93, locale = "ru")
@Composable
private fun RainfallItemPreview() {
    WeatherTheme {
        RainfallItem(
            last24hoursAmount = 0.7,
            next24hoursAmount = 2.4,
            nextOneHourAmount = 0.2,
            isRainingExpected = true
        )
    }
}