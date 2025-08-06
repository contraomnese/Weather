package com.contraomnese.weather.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding24

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        SearchTextField(
            searchQuery = uiState.city.value,
            onSearchQueryChanged = { onEvent(HomeEvent.CityChanged(it)) },
            isError = !uiState.city.isValidCity(),
            modifier = Modifier.padding(padding16)
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = padding24)
        ) {
            uiState.cities.forEach { city ->
                Text(
                    text = "${city.name}, ${city.countryName}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier
                    .height(itemThickness2)
                    .fillMaxWidth())
            }
        }
    }
}