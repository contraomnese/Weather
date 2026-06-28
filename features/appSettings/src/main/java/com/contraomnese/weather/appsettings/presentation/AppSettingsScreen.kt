package com.contraomnese.weather.appsettings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.core.ui.widgets.AnimatedGradientBackgroundScaffold
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.PreferencesSwitcher
import com.contraomnese.weather.core.ui.widgets.RadioGroup
import com.contraomnese.weather.core.ui.widgets.SettingsSection
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemWidth64
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding24
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.presentation.utils.handleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun AppSettingsRoute(viewModel: AppSettingsViewModel) {
    val snackBarHostState = LocalSnackbarHostState.current

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    AppSettingsPage(
        uiState,
        viewModel.eventFlow,
        pushAction = viewModel::push,
        snackBarHostState
    )
}

@Composable
private fun AppSettingsPage(
    uiState: AppSettingsScreenState,
    eventFlow: Flow<AppSettingsEvent> = emptyFlow(),
    pushAction: (AppSettingsAction) -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    AnimatedGradientBackgroundScaffold(
        snackBarHostState,
        insets = WindowInsets.statusBars
    ) {
        when {
            uiState.isLoading ->
                LoadingIndicator(
                    Modifier
                        .align(Alignment.Center)
                        .width(itemWidth64)
                )

            else -> {
                AppSettingsScreen(
                    uiState = uiState,
                    eventFlow,
                    pushAction = pushAction,
                    snackBarHostState = snackBarHostState
                )
            }
        }
    }
}

@Composable
private fun AppSettingsScreen(
    uiState: AppSettingsScreenState,
    eventFlow: Flow<AppSettingsEvent>,
    pushAction: (AppSettingsAction) -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    val context = LocalContext.current

    eventFlow.collectEvent { event ->
        when (event) {
            is AppSettingsEvent.HandleError -> snackBarHostState.showSnackbar(
                message = event.cause.handleError(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = padding16),
        contentPadding = PaddingValues(vertical = padding24),
        verticalArrangement = Arrangement.spacedBy(padding24)
    ) {
        item {
            SettingsSection(title = stringResource(R.string.settings_units_title)) {
                RadioGroup(
                    title = stringResource(R.string.temperature_title),
                    options = TemperatureUnit.entries.toList(),
                    selected = uiState.temperatureUnit,
                    onSelected = { pushAction(AppSettingsAction.TemperatureUnitChange(it)) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                RadioGroup(
                    title = stringResource(R.string.speed_title),
                    options = WindSpeedUnit.entries.toList(),
                    selected = uiState.windSpeedUnit,
                    onSelected = { pushAction(AppSettingsAction.WindSpeedUnitChange(it)) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                RadioGroup(
                    title = stringResource(R.string.precipitation_title),
                    options = PrecipitationUnit.entries.toList(),
                    selected = uiState.precipitationUnit,
                    onSelected = { pushAction(AppSettingsAction.PrecipitationUnitChange(it)) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                RadioGroup(
                    title = stringResource(R.string.atm_pressure_title),
                    options = PressureUnit.entries.toList(),
                    selected = uiState.pressureUnit,
                    onSelected = { pushAction(AppSettingsAction.PressureUnitChange(it)) }
                )
            }
        }

        item {
            SettingsSection(title = stringResource(R.string.settings_preferences_title)) {
                PreferencesSwitcher(
                    title = R.string.forecast_auto_sync_enable,
                    enabled = uiState.forecastAutoSyncEnabled,
                    onEnabled = { pushAction(AppSettingsAction.ForecastAutoSyncChange(it)) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                PreferencesSwitcher(
                    title = R.string.push_notifications_enable,
                    enabled = uiState.pushNotificationsEnabled,
                    onEnabled = { pushAction(AppSettingsAction.PushNotificationsChange(it)) }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF3F6E74, locale = "ru")
@Preview(showBackground = true, backgroundColor = 0xFF3F6E74, locale = "en")
@Composable
private fun AppSettingsPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = AppSettingsScreenState(
        isLoading = false,
        temperatureUnit = TemperatureUnit.Celsius,
        pressureUnit = PressureUnit.MmHg,
        precipitationUnit = PrecipitationUnit.Millimeters,
        windSpeedUnit = WindSpeedUnit.Mph,
        forecastAutoSyncEnabled = true,
        pushNotificationsEnabled = false,
    )

    WeatherTheme {
        AppSettingsPage(
            uiState = uiState,
            eventFlow = emptyFlow(),
            pushAction = {},
            snackbarHostState
        )
    }
}