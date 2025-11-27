package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.favorite.toDomain
import com.contraomnese.weather.data.mappers.favorite.toEntity
import com.contraomnese.weather.data.mappers.location.toDomain
import com.contraomnese.weather.data.mappers.location.toEntity
import com.contraomnese.weather.data.network.api.LocationsApi
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.exceptions.storageError
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocationsRepositoryImpl(
    private val locationsApi: LocationsApi,
    private val weatherDatabase: WeatherDatabase,
    private val apiParser: ApiParser,
    private val dispatcher: CoroutineDispatcher,
) : LocationsRepository {

    override suspend fun getFavorites(): Result<List<Location>> {

        val favorites = try {
            withContext(dispatcher) {
                weatherDatabase.favoritesDao().getFavorites()
            }
        } catch (throwable: Throwable) {
            return Result.failure(storageError(logPrefix("Impossible get favorites from database"), throwable))
        }

        return try {
            Result.success(favorites.map { it.toDomain() })
        } catch (throwable: Throwable) {
            Result.failure(operationFailed(logPrefix("Impossible convert locations from database"), throwable))
        }
    }

    override fun observeFavorites(): Flow<List<Location>> =
        weatherDatabase
            .favoritesDao()
            .observeFavorites()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(dispatcher)

    override suspend fun addFavorite(locationId: Int): Result<Unit> {

        val location = try {
            withContext(dispatcher) {
                weatherDatabase.matchingLocationsDao().getLocation(locationId)
            }
        } catch (throwable: Throwable) {
            return Result.failure(storageError(logPrefix("Get location from database failed"), throwable))
        }

        val favoriteEntity = try {
            location.toEntity()
        } catch (throwable: Throwable) {
            return Result.failure(operationFailed(logPrefix("Impossible convert location from database"), throwable))
        }

        return try {
            withContext(dispatcher) {
                weatherDatabase.favoritesDao().addFavorite(favoriteEntity)
            }
            Result.success(Unit)
        } catch (throwable: Throwable) {
            Result.failure(storageError(logPrefix("Impossible add favorite to database"), throwable))
        }
    }

    override suspend fun deleteFavorite(id: Int): Result<Unit> {
        return withContext(dispatcher) {
            try {
                weatherDatabase.favoritesDao().removeFavorite(id)
                Result.success(Unit)
            } catch (throwable: Throwable) {
                Result.failure(storageError(logPrefix("Impossible remove favorite to database"), throwable))
            }
        }
    }

    override suspend fun getLocationsByLocationName(name: String): Result<List<Location>> {

        val locations = try {
            withContext(dispatcher) {
                apiParser.parseOrThrowError(locationsApi.getLocations(name))
            }
        } catch (cause: Throwable) {
            return Result.failure(cause)
        }

        val result = try {
            val primary = locations.filter { it.type == "city" || it.type == "town" }
            primary.ifEmpty {
                locations
            }.map { it.toEntity() }
        } catch (throwable: Throwable) {
            return Result.failure(operationFailed(logPrefix("Impossible convert locations from network"), throwable))
        }

        return try {
            withContext(dispatcher) {
                weatherDatabase.matchingLocationsDao().addLocations(result)
            }
            Result.success(result.map { it.toDomain() })
        } catch (throwable: Throwable) {
            Result.failure(operationFailed(logPrefix("Impossible add matching locations to database"), throwable))
        }
    }

    override suspend fun getLocationByCoordinates(lat: Double, lon: Double): Result<Location> {

        val location = try {
            withContext(dispatcher) {
                apiParser.parseOrThrowError(locationsApi.getLocation(latitude = lat, longitude = lon))
            }
        } catch (cause: Throwable) {
            return Result.failure(cause)
        }

        val result = try {
            location.toEntity()
        } catch (throwable: Throwable) {
            return Result.failure(operationFailed(logPrefix("Impossible convert location from network"), throwable))
        }

        return try {
            withContext(dispatcher) {
                weatherDatabase.matchingLocationsDao().addLocation(result)
            }
            Result.success(result.toDomain())
        } catch (throwable: Throwable) {
            Result.failure(operationFailed(logPrefix("Impossible add matching location to database"), throwable))
        }
    }
}