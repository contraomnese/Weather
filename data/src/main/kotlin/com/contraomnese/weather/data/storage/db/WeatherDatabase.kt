package com.contraomnese.weather.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastDao
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAlertEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastHourEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastTodayEntity
import com.contraomnese.weather.data.storage.db.locations.dao.FavoritesDao
import com.contraomnese.weather.data.storage.db.locations.dao.LocationsDao
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contraomnese_weather_app.sqlite3"

@Database(
    entities = [
        FavoriteEntity::class,
        ForecastLocationEntity::class,
        ForecastTodayEntity::class,
        ForecastDailyEntity::class,
        ForecastDayEntity::class,
        ForecastAstroEntity::class,
        ForecastHourEntity::class,
        ForecastAlertEntity::class,
        MatchingLocationEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
    abstract fun forecastDao(): ForecastDao
    abstract fun locationsDao(): LocationsDao

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