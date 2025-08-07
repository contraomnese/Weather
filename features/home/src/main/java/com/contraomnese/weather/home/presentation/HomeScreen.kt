package com.contraomnese.weather.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.SearchTextField
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.cornerRadius1
import com.contraomnese.weather.design.theme.itemHeight20
import com.contraomnese.weather.design.theme.itemHeight40
import com.contraomnese.weather.design.theme.itemThickness2
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToWeatherByLocation: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToWeatherByLocation = onNavigateToWeatherByLocation,
        modifier = modifier
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToWeatherByLocation: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {

    Column {
        SearchTextField(
            modifier = Modifier.padding(padding16),
            searchQuery = uiState.location.value,
            onSearchQueryChanged = { onEvent(HomeEvent.LocationChanged(it)) },
            isError = !uiState.location.isValidLocation(),
            leadingIcon = if (uiState.isLoading) {
                {
                    LoadingIndicator(
                        modifier =
                            Modifier
                                .height(itemHeight20)
                                .aspectRatio(1f)
                    )
                }
            } else null,
            trailingIcon = if (uiState.location.value.isEmpty()) {
                {
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(
                            imageVector = WeatherIcons.Map,
                            contentDescription = null
                        )
                    }
                }
            } else null
        )

        Column(
            modifier = Modifier
                .padding(horizontal = padding16)
        ) {
            uiState.locations.forEach { location ->
                Row(
                    modifier = Modifier
                        .clickable { onNavigateToWeatherByLocation(location.id) }
                        .fillMaxWidth()
                        .height(itemHeight40),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = location.countryName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }

                Spacer(
                    modifier = Modifier
                        .height(itemThickness2)
                        .background(
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                            RoundedCornerShape(cornerRadius1)
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )
        val original = LocationDomainModel(
            id = 1,
            name = "",
            countryName = "",
        )
        val cities = List(10) { index ->
            original.copy(
                id = index,
                name = "City $index",
                countryName = "Country $index"
            )
        }

        HomeScreen(
            uiState = HomeUiState(locations = cities.toPersistentList()),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenLoadingPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )

        HomeScreen(
            uiState = HomeUiState(isLoading = true),
            onEvent = {}
        )
    }
}