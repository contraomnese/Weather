package com.contraomnese.weather.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastDao
import com.contraomnese.weather.data.storage.db.forecast.entities.DayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastCurrentEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.HourlyForecastEntity
import com.contraomnese.weather.data.storage.db.locations.dao.FavoritesDao
import com.contraomnese.weather.data.storage.db.locations.dao.LocationsDao
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.data.storage.db.locations.entities.LocationCountryEntity
import com.contraomnese.weather.data.storage.db.locations.entities.LocationEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contraomnese_weather_app.sqlite3"

@Database(
    entities = [
        LocationEntity::class,
        LocationCountryEntity::class,
        FavoriteEntity::class,
        ForecastLocationEntity::class,
        ForecastCurrentEntity::class,
        ForecastDayEntity::class,
        DayEntity::class,
        ForecastAstroEntity::class,
        HourlyForecastEntity::class,
        ForecastAlertEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun locationsDao(): LocationsDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun forecastDao(): ForecastDao

    companion object {
        fun create(context: Context): WeatherDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = WeatherDatabase::class.java,
                name = DATABASE_NAME
            )
                .createFromAsset(DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}