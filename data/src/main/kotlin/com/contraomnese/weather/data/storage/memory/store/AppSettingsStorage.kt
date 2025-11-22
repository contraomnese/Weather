package com.contraomnese.weather.data.storage.memory.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
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
import java.util.Locale

val Context.appSettingsDataStore by preferencesDataStore("app_settings")

class AppSettingsStorageImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : AppSettingsStorage {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val SPEED_UNIT = stringPreferencesKey("speed_unit")
        val PRECIPITATION_UNIT = stringPreferencesKey("precipitation_unit")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
        val PRESSURE_UNIT = stringPreferencesKey("pressure_unit")
    }

    private lateinit var settings: AppSettingsEntity

    private val _settings = context.appSettingsDataStore.data
        .map { prefs ->
            val languageValue = prefs[Keys.LANGUAGE]
                ?: Locale.getDefault().language

            settings = AppSettingsEntity(
                language = languageValue,
                speedUnit = prefs[Keys.SPEED_UNIT] ?: WindSpeedUnit.Kph.name,
                precipitationUnit = prefs[Keys.PRECIPITATION_UNIT] ?: PrecipitationUnit.Millimeters.name,
                temperatureUnit = prefs[Keys.TEMPERATURE_UNIT] ?: TemperatureUnit.Celsius.name,
                pressureUnit = prefs[Keys.PRESSURE_UNIT] ?: PressureUnit.MmHg.name
            )
            settings
        }
        .shareIn(CoroutineScope(dispatcher), SharingStarted.Eagerly, 1)


    override fun observeSettings(): Flow<AppSettingsEntity> = _settings.distinctUntilChanged()

    override fun getSettings() = settings

    override suspend fun saveSettings(entity: AppSettingsEntity) {
        if (getSettings() != entity) {
            context.appSettingsDataStore.edit { prefs ->
                prefs[Keys.LANGUAGE] = entity.language
                prefs[Keys.SPEED_UNIT] = entity.speedUnit
                prefs[Keys.PRECIPITATION_UNIT] = entity.precipitationUnit
                prefs[Keys.TEMPERATURE_UNIT] = entity.temperatureUnit
                prefs[Keys.PRESSURE_UNIT] = entity.pressureUnit
            }
        }
    }
}