package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.favorite.FavoriteMapper
import com.contraomnese.weather.data.mappers.locations.toDomain
import com.contraomnese.weather.data.mappers.locations.toEntity
import com.contraomnese.weather.data.network.api.LocationsApi
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
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
    private val database: WeatherAppDatabase,
    private val apiParser: ApiParser,
    private val favoriteMapper: FavoriteMapper = FavoriteMapper(),
    private val dispatcher: CoroutineDispatcher,
) : LocationsRepository {

    override suspend fun getFavorites(): Result<List<Location>> {

        val favorites = try {
            withContext(dispatcher) {
                database.favoritesDao().getFavorites()
            }
        } catch (cause: Exception) {
            return Result.failure(storageError(logPrefix("Impossible get favorites from database"), cause))
        }

        return try {
            Result.success(favorites.map { favoriteMapper.toDomain(it) })
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible convert locations from database"), cause))
        }
    }

    override fun observeFavorites(): Flow<List<Location>> =
        database
            .favoritesDao()
            .observeFavorites()
            .map { list -> list.map { favoriteMapper.toDomain(it) } }
            .flowOn(dispatcher)

    override suspend fun addFavorite(locationId: Int): Result<Int> = withContext(dispatcher) {

        try {
            database.favoritesDao().addFavorite(locationId)
            Result.success(locationId)
        } catch (cause: Exception) {
            Result.failure(storageError(logPrefix("Impossible add favorite to database"), cause))
        }
    }

    override suspend fun deleteFavorite(id: Int): Result<Unit> {
        return withContext(dispatcher) {
            try {
                database.favoritesDao().removeFavorite(id)
                Result.success(Unit)
            } catch (cause: Exception) {
                Result.failure(storageError(logPrefix("Impossible remove favorite to database"), cause))
            }
        }
    }

    override suspend fun getLocationsByLocationName(name: String): Result<List<Location>> {

        val locations = try {
            withContext(dispatcher) {
                apiParser.parseOrThrowError(locationsApi.getLocations(name))
            }
        } catch (cause: Exception) {
            return Result.failure(cause)
        }

        val result = try {
            val primary = locations.filter { it.type == "city" || it.type == "town" }
            primary
                .ifEmpty { locations }
                .map { it.toEntity() }
        } catch (cause: Exception) {
            return Result.failure(operationFailed(logPrefix("Impossible convert locations from network"), cause))
        }

        return try {
            withContext(dispatcher) {
                database.locationsDao().insertMatchingLocations(result)
            }
            Result.success(result.map { it.toDomain() })
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible add matching locations to database"), cause))
        }
    }

    override suspend fun getLocationByCoordinates(lat: Double, lon: Double): Result<Location> {

        val location = try {
            withContext(dispatcher) {
                apiParser.parseOrThrowError(locationsApi.getLocation(latitude = lat, longitude = lon))
            }
        } catch (cause: Exception) {
            return Result.failure(cause)
        }

        val result = try {
            location.toEntity()
        } catch (cause: Exception) {
            return Result.failure(operationFailed(logPrefix("Impossible convert location from network"), cause))
        }

        return try {
            withContext(dispatcher) {
                database.locationsDao().insertMatchingLocation(result)
            }
            Result.success(result.toDomain())
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible add matching location to database"), cause))
        }
    }
}