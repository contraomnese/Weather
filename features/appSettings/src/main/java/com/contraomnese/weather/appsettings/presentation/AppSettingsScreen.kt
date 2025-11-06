package com.contraomnese.weather.appsettings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.core.ui.composition.LocalSnackbarHostState
import com.contraomnese.weather.core.ui.widgets.LoadingIndicator
import com.contraomnese.weather.core.ui.widgets.WeatherSnackBarHost
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemWidth64
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.presentation.architecture.collectEvent
import com.contraomnese.weather.presentation.utils.handleError
import kotlinx.coroutines.flow.Flow

@Composable
internal fun AppSettingsRoute(
    viewModel: AppSettingsViewModel,
    eventFlow: Flow<AppSettingsEvent>,
    pushAction: (AppSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackBarHostState = LocalSnackbarHostState.current

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    eventFlow.collectEvent { event ->
        when (event) {
            is AppSettingsEvent.HandleError -> snackBarHostState.showSnackbar(
                message = event.cause.handleError(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { WeatherSnackBarHost(snackBarHostState) },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        pushAction = pushAction,
                    )
                }
            }
        }
    }

}

@Composable
private fun AppSettingsScreen(
    modifier: Modifier = Modifier,
    uiState: AppSettingsScreenState,
    pushAction: (AppSettingsAction) -> Unit,
) {
    requireNotNull(uiState.appSettings)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = padding16),
        verticalArrangement = Arrangement.spacedBy(padding8)
    ) {
        RadioGroup(
            title = stringResource(R.string.temperature_title),
            options = TemperatureUnit.entries.toList(),
            selected = uiState.appSettings.temperatureUnit,
            onSelected = {
                pushAction(AppSettingsAction.TemperatureUnitChange(it))
            }
        )

        RadioGroup(
            title = stringResource(R.string.speed_title),
            options = WindSpeedUnit.entries.toList(),
            selected = uiState.appSettings.windSpeedUnit,
            onSelected = {
                pushAction(AppSettingsAction.WindSpeedUnitChange(it))
            }
        )

        RadioGroup(
            title = stringResource(R.string.precipitation_title),
            options = PrecipitationUnit.entries.toList(),
            selected = uiState.appSettings.precipitationUnit,
            onSelected = {
                pushAction(AppSettingsAction.PrecipitationUnitChange(it))
            }
        )

        RadioGroup(
            title = stringResource(R.string.atm_pressure_title),
            options = PressureUnit.entries.toList(),
            selected = uiState.appSettings.pressureUnit,
            onSelected = {
                pushAction(AppSettingsAction.PressureUnitChange(it))
            }
        )
    }
}

@Composable
private inline fun <reified T : Enum<T>> RadioGroup(
    title: String,
    options: List<T>,
    selected: T,
    crossinline onSelected: (T) -> Unit,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = padding4)
        )
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selected),
                        onClick = { onSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = padding8),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selected),
                    onClick = null
                )
                Text(
                    text = stringResource(option.toTextRes()),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineBreak = LineBreak.Heading
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = padding8)
                )
            }
        }
    }
}

inline fun <reified T : Enum<T>> T.toTextRes(): Int = when (this) {
    is TemperatureUnit -> when (this) {
        TemperatureUnit.Celsius -> R.string.temperature_celsius
        TemperatureUnit.Fahrenheit -> R.string.temperature_fahrenheit
    }

    is WindSpeedUnit -> when (this) {
        WindSpeedUnit.Kph -> R.string.speed_kph
        WindSpeedUnit.Mph -> R.string.speed_mph
        WindSpeedUnit.Ms -> R.string.speed_ms
    }

    is PrecipitationUnit -> when (this) {
        PrecipitationUnit.Millimeters -> R.string.precipitation_mm
        PrecipitationUnit.Inches -> R.string.precipitation_in
    }

    is PressureUnit -> when (this) {
        PressureUnit.MmHg -> R.string.pressure_mmhg
        PressureUnit.GPa -> R.string.pressure_gpa
    }

    else -> error("Unsupported enum type: ${T::class.simpleName}")
}


@Preview(showBackground = true, backgroundColor = 0xFF3F6E74)
@Composable
private fun AppSettingsPreview() {
    WeatherTheme {
        AppSettingsScreen(
            uiState = AppSettingsScreenState(
                appSettings = AppSettings()
            ),
            pushAction = {}
        )
    }
}