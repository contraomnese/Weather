package com.contraomnese.weather.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.theme.padding16

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
    SearchTextField(
        searchQuery = "London",
        modifier = Modifier.padding(padding16)
    )
}