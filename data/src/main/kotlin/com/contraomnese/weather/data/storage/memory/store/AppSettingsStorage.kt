package com.contraomnese.weather.data.storage.memory.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PrecipitationUnit
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.model.WindSpeedUnit
import com.contraomnese.weather.domain.scheduler.ForecastUpdateScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes

val Context.appSettingsDataStore by preferencesDataStore("app_settings")
val DEFAULT_FAVORITES_FORECAST_UPDATE_INTERVAL = TimeUnit.HOURS.toMillis(3)

class AppSettingsStorageImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val forecastUpdateScheduler: ForecastUpdateScheduler,
) : AppSettingsStorage {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val TIMEZONE = stringPreferencesKey("timezone")
        val SPEED_UNIT = stringPreferencesKey("speed_unit")
        val PRECIPITATION_UNIT = stringPreferencesKey("precipitation_unit")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
        val PRESSURE_UNIT = stringPreferencesKey("pressure_unit")
        val FORECAST_UPDATE_ENABLED = booleanPreferencesKey("forecast_auto_sync_enabled")
        val FAVORITES_FORECAST_UPDATE_INTERVAL = longPreferencesKey("favorites_forecast_update_interval")
        val FAVORITES_FORECAST_NOTIFICATION_ENABLED = booleanPreferencesKey("favorites_forecast_notification_enabled")
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
                favoritesForecastUpdateEnabled = prefs[Keys.FORECAST_UPDATE_ENABLED] ?: true,
                favoritesForecastUpdateInterval = readFavoritesForecastUpdateInterval(),
                favoritesForecastNotificationEnabled = readFavoritesForecastNotificationEnabled()
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
                prefs[Keys.FORECAST_UPDATE_ENABLED] = settings.favoritesForecastUpdateEnabled
                prefs[Keys.FAVORITES_FORECAST_NOTIFICATION_ENABLED] = settings.favoritesForecastNotificationEnabled
            }
        }
    }

    override suspend fun saveLanguage(language: Language) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.value
        }
    }

    override suspend fun saveTimezone(timezone: String) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.TIMEZONE] = timezone
        }
    }

    override suspend fun saveTemperatureUnit(unit: TemperatureUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.TEMPERATURE_UNIT] = unit.name
        }
    }

    override suspend fun savePrecipitationUnit(unit: PrecipitationUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.PRECIPITATION_UNIT] = unit.name
        }
    }

    override suspend fun savePressureUnit(unit: PressureUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.PRESSURE_UNIT] = unit.name
        }
    }

    override suspend fun saveWindSpeedUnit(unit: WindSpeedUnit) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.SPEED_UNIT] = unit.name
        }
    }

    override suspend fun saveFavoritesForecastUpdateEnabled(enabled: Boolean) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.FORECAST_UPDATE_ENABLED] = enabled
        }
        launchFavoritesForecastUpdate(enabled)
    }

    override suspend fun saveFavoritesForecastNotificationEnabled(enabled: Boolean) {
        context.appSettingsDataStore.edit { prefs ->
            prefs[Keys.FAVORITES_FORECAST_NOTIFICATION_ENABLED] = enabled
        }
    }

    override suspend fun readFavoritesForecastNotificationEnabled(): Boolean = context.appSettingsDataStore.data
        .map { preferences ->
            preferences[Keys.FAVORITES_FORECAST_NOTIFICATION_ENABLED] ?: true
        }.first()

    override suspend fun saveFavoritesForecastUpdateInterval(selectedTime: Long) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.FAVORITES_FORECAST_UPDATE_INTERVAL] = selectedTime
        }
    }

    override suspend fun readFavoritesForecastUpdateInterval(): Long = context.appSettingsDataStore.data
        .map { preferences ->
            preferences[Keys.FAVORITES_FORECAST_UPDATE_INTERVAL] ?: DEFAULT_FAVORITES_FORECAST_UPDATE_INTERVAL
        }.first()

    private suspend fun launchFavoritesForecastUpdate(enabled: Boolean) {
        if (enabled) {
            forecastUpdateScheduler.scheduleFavoritesUpdate(
                intervalMs = readFavoritesForecastUpdateInterval(),
                initialDelayMs = getDelayUntilMidnight()
            )
        } else {
            forecastUpdateScheduler.stopFavoritesUpdate()
        }
    }

    private fun getDelayUntilMidnight(): Long {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()

        val today = now.toLocalDateTime(timeZone).date

        val tomorrowMidnight = today
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)
            .plus(15.minutes)

        return (tomorrowMidnight - now).inWholeMilliseconds
    }
}