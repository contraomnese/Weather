package com.contraomnese.weather.data.storage.memory.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.datetime.TimeZone
import java.util.Locale

val Context.appSettingsDataStore by preferencesDataStore("app_settings")

class AppSettingsStorageImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : AppSettingsStorage {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val TIMEZONE = stringPreferencesKey("timezone")
        val SPEED_UNIT = stringPreferencesKey("speed_unit")
        val PRECIPITATION_UNIT = stringPreferencesKey("precipitation_unit")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
        val PRESSURE_UNIT = stringPreferencesKey("pressure_unit")
        val FORECAST_AUTO_SYNC_ENABLED = booleanPreferencesKey("forecast_auto_sync_enabled")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }

    private lateinit var settings: AppSettingsEntity

    private val _settings = context.appSettingsDataStore.data
        .map { prefs ->
            val languageValue = prefs[Keys.LANGUAGE]
                ?: Locale.getDefault().language

            val timezoneValue = prefs[Keys.TIMEZONE]
                ?: TimeZone.currentSystemDefault().toString()

            settings = AppSettingsEntity(
                language = languageValue,
                timezone = timezoneValue,
                speedUnit = prefs[Keys.SPEED_UNIT] ?: WindSpeedUnit.Kph.name,
                precipitationUnit = prefs[Keys.PRECIPITATION_UNIT] ?: PrecipitationUnit.Millimeters.name,
                temperatureUnit = prefs[Keys.TEMPERATURE_UNIT] ?: TemperatureUnit.Celsius.name,
                pressureUnit = prefs[Keys.PRESSURE_UNIT] ?: PressureUnit.MmHg.name,
                forecastAutoSyncEnabled = prefs[Keys.FORECAST_AUTO_SYNC_ENABLED] ?: true,
                notificationsEnabled = prefs[Keys.NOTIFICATIONS_ENABLED] ?: true
            )
            settings
        }
        .shareIn(CoroutineScope(dispatcher), SharingStarted.Eagerly, 1)


    override fun observeSettings(): Flow<AppSettingsEntity> = _settings.distinctUntilChanged()

    override fun getSettings() = settings

    override suspend fun saveSettings(settings: AppSettingsEntity) {
        if (getSettings() != settings) {
            context.appSettingsDataStore.edit { prefs ->
                prefs[Keys.LANGUAGE] = settings.language
                prefs[Keys.TIMEZONE] = settings.timezone
                prefs[Keys.SPEED_UNIT] = settings.speedUnit
                prefs[Keys.PRECIPITATION_UNIT] = settings.precipitationUnit
                prefs[Keys.TEMPERATURE_UNIT] = settings.temperatureUnit
                prefs[Keys.PRESSURE_UNIT] = settings.pressureUnit
                prefs[Keys.FORECAST_AUTO_SYNC_ENABLED] = settings.forecastAutoSyncEnabled
                prefs[Keys.NOTIFICATIONS_ENABLED] = settings.notificationsEnabled
            }
        }
    }

    override suspend fun setLanguage(language: Language) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.value
        }
    }

    override suspend fun setTimezone(timezone: String) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.TIMEZONE] = timezone
        }
    }

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.TEMPERATURE_UNIT] = unit.name
        }
    }

    override suspend fun setPrecipitationUnit(unit: PrecipitationUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.PRECIPITATION_UNIT] = unit.name
        }
    }

    override suspend fun setPressureUnit(unit: PressureUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.PRESSURE_UNIT] = unit.name
        }
    }

    override suspend fun setWindSpeedUnit(unit: WindSpeedUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.SPEED_UNIT] = unit.name
        }
    }

    override suspend fun setForecastAutoSyncEnabled(enabled: Boolean) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.FORECAST_AUTO_SYNC_ENABLED] = enabled
        }
    }

    override suspend fun setNotificationEnabled(enabled: Boolean) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] = enabled
        }
    }
}