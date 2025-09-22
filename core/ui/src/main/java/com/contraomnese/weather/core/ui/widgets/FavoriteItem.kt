package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.cornerRadius8
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space16
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition
import com.contraomnese.weather.domain.weatherByLocation.model.internal.LocationDateTime
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun FavoriteItem(
    modifier: Modifier = Modifier,
    locationName: String,
    locationCountry: String,
    timeZone: TimeZone,
    conditionText: String,
    condition: CompactWeatherCondition,
    temperature: String,
    maxTemperature: String,
    minTemperature: String,
    onTapClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {

    var showDelete by remember { mutableStateOf(false) }

    var localTime by remember { mutableStateOf("") }

    LaunchedEffect(timeZone) {
        while (true) {
            val instant = Clock.System.now().toLocalDateTime(timeZone)
            localTime = LocationDateTime(instant).toLocalTime()
            val millisToNextMinute = 60_000L - instant.second * 1000
            delay(millisToNextMinute)
        }
    }

    Box {
        Surface(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showDelete = !showDelete
                        },
                        onTap = {
                            onTapClicked()
                        }
                    )
                }
                .drawBehind {
                    val shadowColor = Color.Black.copy(alpha = 0.25f)
                    drawRoundRect(
                        color = shadowColor,
                        topLeft = Offset(10f, 10f),
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(cornerRadius16.toPx(), cornerRadius16.toPx())
                    )
                },
            shape = RoundedCornerShape(cornerRadius16),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 1.0f)
        ) {
            ImageBackground(condition = condition)
            Box(modifier = Modifier.padding(padding16)) {

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(2f),
                        verticalArrangement = Arrangement.spacedBy(space16)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(space8)
                        ) {
                            Text(
                                text = locationName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                modifier = Modifier.wrapContentWidth(),
                                text = localTime,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                        }
                        Text(
                            text = locationCountry,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.temperature, temperature),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space8)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = conditionText,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier
                            .weight(1.5f)
                            .align(Alignment.Bottom),
                        text = stringResource(R.string.temperature_high_low, maxTemperature, minTemperature),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        }
        if (showDelete) {
            IconButton(
                onClick = {
                    onDeleteClicked()
                    showDelete = false
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = padding8, y = -padding8)
                    .background(
                        color = Color.Red,
                        shape = RoundedCornerShape(cornerRadius8)
                    )
            ) {
                Icon(
                    imageVector = WeatherIcons.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true, locale = "ru")
@Composable
fun FavoriteItemPreview() {
    WeatherTheme {
        FavoriteItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight160),
            locationName = "New York",
            locationCountry = "USA USA USA USA USA",
            timeZone = TimeZone.of("Europe/Moscow"),
            conditionText = "Ясно",
            condition = CompactWeatherCondition.CLEAR,
            temperature = "21",
            maxTemperature = "25",
            minTemperature = "16",
            onTapClicked = {},
            onDeleteClicked = {}
        )
    }
}