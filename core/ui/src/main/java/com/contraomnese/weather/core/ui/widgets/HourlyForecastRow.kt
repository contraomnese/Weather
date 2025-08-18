package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.contraomnese.weather.design.theme.cornerRadius1
import com.contraomnese.weather.design.theme.cornerRadius16
import com.contraomnese.weather.design.theme.itemThickness1
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding2
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.design.theme.space8

@Composable
fun HourlyForecastRow(items: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(bottomStart = cornerRadius16, bottomEnd = cornerRadius16)
            )
            .padding(padding8)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = padding16),
            horizontalArrangement = Arrangement.spacedBy(space8)
        ) {
            items(items) { hour ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = hour, style = MaterialTheme.typography.bodySmall)
                    Spacer(
                        modifier = Modifier
                            .height(itemThickness1)
                            .background(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                RoundedCornerShape(cornerRadius1)
                            )
                            .fillMaxWidth()
                            .padding(top = padding2)
                    )
                }
            }
        }
    }
}