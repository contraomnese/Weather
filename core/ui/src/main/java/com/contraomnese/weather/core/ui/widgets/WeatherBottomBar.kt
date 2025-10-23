package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.contraomnese.weather.design.theme.itemHeight48
import com.contraomnese.weather.design.theme.itemWidth56
import com.contraomnese.weather.design.theme.padding8

@Composable
fun WeatherBottomBar(
    favorites: List<Int>,
    currentFavoriteIndex: Int,
    onHomeButtonClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
) {

    BottomAppBar(
        modifier = Modifier
            .height(itemHeight48)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(itemWidth56))
            if (currentFavoriteIndex >= 0) {
                Pointer(favorites, currentFavoriteIndex)
            } else FavoriteButton { onFavoriteButtonClicked() }
            HomeButton { onHomeButtonClicked() }
        }
    }
}