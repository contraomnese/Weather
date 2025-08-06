package com.contraomnese.weather.data.storage.db.locations

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.contraomnese.weather.data.storage.db.locations.dao.LocationsDao
import com.contraomnese.weather.data.storage.db.locations.entities.CityEntity
import com.contraomnese.weather.data.storage.db.locations.entities.CountryEntity

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "locations.sqlite3"

@Database(
    entities = [CityEntity::class, CountryEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class LocationsDatabase : RoomDatabase() {

    abstract fun locationsDao(): LocationsDao

    companion object {
        fun create(context: Context): LocationsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = LocationsDatabase::class.java,
                name = DATABASE_NAME
            )
                .createFromAsset(DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}