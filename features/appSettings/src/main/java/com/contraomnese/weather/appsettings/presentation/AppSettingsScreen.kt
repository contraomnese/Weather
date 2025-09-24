package com.contraomnese.weather.appsettings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit

@Composable
internal fun AppSettingsRoute(
    viewModel: AppSettingsViewModel,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun AppSettingsScreen(
    modifier: Modifier = Modifier,
    uiState: AppSettingsUiState,
    onEvent: (AppSettingsEvent) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = padding16)
            .padding(WindowInsets.statusBars.asPaddingValues()),
        verticalArrangement = Arrangement.spacedBy(padding8)
    ) {
        RadioGroup(
            title = stringResource(R.string.temperature_title),
            options = TemperatureUnit.entries.toList(),
            selected = uiState.appSettings.temperatureUnit,
            onSelected = {
                onEvent(AppSettingsEvent.TemperatureUnitChanged(it))
            }
        )

        RadioGroup(
            title = stringResource(R.string.speed_title),
            options = WindSpeedUnit.entries.toList(),
            selected = uiState.appSettings.windSpeedUnit,
            onSelected = {
                onEvent(AppSettingsEvent.WindSpeedUnitChanged(it))
            }
        )

        RadioGroup(
            title = stringResource(R.string.precipitation_title),
            options = PrecipitationUnit.entries.toList(),
            selected = uiState.appSettings.precipitationUnit,
            onSelected = {
                onEvent(AppSettingsEvent.PrecipitationUnitChanged(it))
            }
        )

        RadioGroup(
            title = stringResource(R.string.atm_pressure_title),
            options = PressureUnit.entries.toList(),
            selected = uiState.appSettings.pressureUnit,
            onSelected = {
                onEvent(AppSettingsEvent.PressureUnitChanged(it))
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
            uiState = AppSettingsUiState(
                appSettings = AppSettings()
            ),
            onEvent = {}
        )
    }
}