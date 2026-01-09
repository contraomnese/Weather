package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.itemWidth56
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space8
import com.contraomnese.weather.domain.weatherByLocation.model.LocationDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun FavoriteItem(
    modifier: Modifier = Modifier,
    @DrawableRes background: Int,
    locationName: String,
    locationCountry: String,
    temperature: String,
    maxTemperature: String,
    minTemperature: String,
    conditionText: String,
    currentTime: Instant,
    timeZone: TimeZone,
    onTapClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    var deleteVisible by remember { mutableStateOf(false) }

    val localTime = remember(currentTime, timeZone) {
        LocationDateTime(currentTime.toLocalDateTime(timeZone)).toLocalTime()
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
                .graphicsLayer {
                    shadowElevation = 12f
                    shape = RoundedCornerShape(cornerRadius16)
                    clip = true
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            deleteVisible = !deleteVisible
                        },
                        onTap = {
                            onTapClicked()
                        }
                    )
                },
            shape = RoundedCornerShape(cornerRadius16),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            color = MaterialTheme.colorScheme.surface
        ) {
            ImageBackground(backgroundResId = background)
            BodySection(
                locationName,
                localTime,
                locationCountry,
                temperature,
                conditionText,
                maxTemperature,
                minTemperature
            )
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
                    .graphicsLayer {
                        shadowElevation = 12f
                        shape = RoundedCornerShape(cornerRadius16)
                        clip = true
                    },
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
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
    Box(
        modifier = Modifier
            .padding(padding16)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = padding8),
            ) {
                Text(
                    text = locationName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,

                    )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space8)
                ) {
                    Text(
                        text = locationCountry,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .wrapContentWidth(Alignment.Start)
                    )

                    Text(
                        text = localTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.wrapContentWidth(Alignment.End)
                    )
                }
            }

            Text(
                text = stringResource(R.string.temperature, temperature),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = conditionText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(R.string.temperature_high_low, maxTemperature, minTemperature),
                textAlign = TextAlign.End,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom)
            )
        }
    }
}


@Preview(showBackground = true, locale = "ru", device = "id:pixel_5", showSystemUi = false, backgroundColor = 0xFF277BB6)
@Composable
fun FavoriteItemPreview() {
    WeatherTheme {
        FavoriteItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding16)
                .height(itemHeight160),
            locationName = "New York New York New York",
            locationCountry = "USA USA USA USA USA USA USA USA USA USA",
            timeZone = TimeZone.of("Europe/Moscow"),
            conditionText = "Ясно Ясно Ясно Ясно Ясно Ясно",
            background = R.drawable.clear,
            temperature = "21",
            maxTemperature = "25",
            minTemperature = "16",
            currentTime = Clock.System.now(),
            onTapClicked = {},
            onDeleteClicked = {}
        )
    }
}