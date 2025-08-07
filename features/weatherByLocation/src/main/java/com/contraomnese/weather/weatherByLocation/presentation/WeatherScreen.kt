package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.padding12
import com.contraomnese.weather.design.theme.padding8

@Composable
internal fun WeatherRoute(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> LoadingIndicator(modifier = Modifier.height(itemHeight64))
        else -> WeatherScreen(
            uiState = uiState, modifier = modifier
        )
    }
}

@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(top = padding12),
                text = uiState.location.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier
                    .padding(top = padding8),
                text = uiState.weather!!.currentTemperature,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}