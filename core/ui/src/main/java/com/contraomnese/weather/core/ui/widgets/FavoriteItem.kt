package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.cornerRadius8
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8

@Composable
fun FavoriteItem(
    modifier: Modifier = Modifier,
    locationName: String,
    locationCountry: String = "Russia",
    locationTime: String = "12:12",
    condition: String = "Clear",
    temperature: String = "21",
    maxTemperature: String = "25",
    minTemperature: String = "16",
    onTapClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {

    var showDelete by remember { mutableStateOf(false) }
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
            Box(
                modifier = Modifier.padding(padding16)
            ) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = locationCountry,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = locationTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.BottomStart),
                    text = condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = stringResource(R.string.temperature, temperature),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    text = stringResource(R.string.temperature_high_low, maxTemperature, minTemperature),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
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

@Preview(showBackground = true)
@Composable
fun FavoriteItemPreview() {
    WeatherTheme {
        FavoriteItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight140),
            locationName = "New York",
            locationCountry = "USA",
            onTapClicked = {},
            onDeleteClicked = {}
        )
    }
}