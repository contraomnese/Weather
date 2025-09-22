package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemWidth160
import com.contraomnese.weather.design.theme.itemWidth56
import com.contraomnese.weather.design.theme.padding16
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

    var deleteVisible by remember { mutableStateOf(false) }

    var localTime by remember { mutableStateOf("") }

    LaunchedEffect(timeZone) {
        while (true) {
            val instant = Clock.System.now().toLocalDateTime(timeZone)
            localTime = LocationDateTime(instant).toLocalTime()
            val millisToNextMinute = 60_000L - instant.second * 1000
            delay(millisToNextMinute)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(space8)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            deleteVisible = !deleteVisible
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
                        topLeft = Offset(4f, 8f),
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(cornerRadius16.toPx(), cornerRadius16.toPx())
                    )
                },
            shape = RoundedCornerShape(cornerRadius16),
            color = MaterialTheme.colorScheme.surface
        ) {
            ImageBackground(condition = condition)
            BodySection(locationName, localTime, locationCountry, temperature, conditionText, maxTemperature, minTemperature)
        }
        AnimatedVisibility(
            visible = deleteVisible,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                initialOffsetX = { fullWidth -> fullWidth / 2 }),
            exit = slideOutHorizontally(
                animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                targetOffsetX = { fullWidth -> fullWidth / 2 }),
        ) {
            Surface(
                modifier = Modifier
                    .width(itemWidth56)
                    .fillMaxHeight()
                    .drawBehind {
                        val shadowColor = Color.Black.copy(alpha = 0.25f)
                        drawRoundRect(
                            color = shadowColor,
                            topLeft = Offset(4f, 8f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(cornerRadius16.toPx(), cornerRadius16.toPx())
                        )
                    },
                shape = RoundedCornerShape(cornerRadius16),
            ) {
                IconButton(
                    onClick = {
                        onDeleteClicked()
                        deleteVisible = false
                    }
                ) {
                    Icon(
                        imageVector = WeatherIcons.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }

    }
}

@Composable
private fun BodySection(
    locationName: String,
    localTime: String,
    locationCountry: String,
    temperature: String,
    conditionText: String,
    maxTemperature: String,
    minTemperature: String,
) {
    Box(modifier = Modifier.padding(padding16)) {

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(padding16)
        ) {
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(space8)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space8)
                ) {
                    Text(
                        modifier = Modifier.widthIn(max = itemWidth160),
                        text = locationName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.width(itemWidth56),
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
                modifier = Modifier.wrapContentWidth(),
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
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom),
                text = stringResource(R.string.temperature_high_low, maxTemperature, minTemperature),
                textAlign = TextAlign.End,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
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
            locationName = "New York New York New York",
            locationCountry = "USA USA USA USA USA",
            timeZone = TimeZone.of("Europe/Moscow"),
            conditionText = "Ясно Ясно Ясно Ясно Ясно Ясно",
            condition = CompactWeatherCondition.CLEAR,
            temperature = "21",
            maxTemperature = "25",
            minTemperature = "16",
            onTapClicked = {},
            onDeleteClicked = {}
        )
    }
}