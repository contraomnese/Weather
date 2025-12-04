package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.favorite.toDomain
import com.contraomnese.weather.data.mappers.locations.toDomain
import com.contraomnese.weather.data.mappers.locations.toEntity
import com.contraomnese.weather.data.network.api.LocationsApi
import com.contraomnese.weather.data.network.models.MatchingLocationNetwork
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
import com.contraomnese.weather.data.storage.db.locations.entities.FavoriteEntity
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity
import com.contraomnese.weather.domain.exceptions.badRequest
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
import retrofit2.Response

class LocationsRepositoryImpl(
    private val locationsApi: LocationsApi,
    private val database: WeatherAppDatabase,
    private val apiParser: ApiParser,
    private val dispatcher: CoroutineDispatcher,
) : LocationsRepository {

    override suspend fun getFavorites(): Result<List<Location>> =
        getFavoritesResult()
            .mapCatching { entities -> entities.toDomain() }

    override fun observeFavorites(): Flow<List<Location>> =
        database
            .favoritesDao()
            .observeFavorites()
            .map { favorites -> favorites.map { it.toDomain() } }
            .flowOn(dispatcher)

    override suspend fun addFavorite(locationId: Int): Result<Int> = withContext(dispatcher) {

        try {
            database.favoritesDao().addFavorite(locationId)
            Result.success(locationId)
        } catch (cause: Exception) {
            Result.failure(storageError(logPrefix("Impossible add favorite to storage"), cause))
        }
    }

    override suspend fun deleteFavorite(locationId: Int): Result<Int> = withContext(dispatcher) {
        try {
            database.favoritesDao().removeFavorite(locationId)
            Result.success(locationId)
        } catch (cause: Exception) {
            Result.failure(storageError(logPrefix("Impossible remove favorite to storage"), cause))
        }
    }

    override suspend fun getLocationsByName(query: String): Result<List<Location>> =
        getMatchingLocations(query)
            .mapCatching { network -> network.toEntity() }
            .mapCatching { entities ->
                insertMatchingLocations(entities)
                entities.toDomain()
            }

    override suspend fun getLocationByCoordinates(latitude: Double, longitude: Double): Result<Location> =
        getMatchingLocation(latitude = latitude, longitude = longitude)
            .mapCatching { it.toEntity() }
            .mapCatching { entity ->
                insertMatchingLocation(entity)
                entity.toDomain()
            }

    private suspend fun getFavoritesResult(): Result<List<FavoriteEntity>> =
        withContext(dispatcher) {
            try {
                Result.success(database.favoritesDao().getFavorites())
            } catch (cause: Exception) {
                Result.failure(storageError(logPrefix("Impossible get favorites from storage"), cause))
            }
        }

    private suspend fun getMatchingLocations(query: String) = withContext(dispatcher) {
        try {
            parseMatchingLocations(locationsApi.getLocations(query))
        } catch (cause: Exception) {
            throw badRequest(logPrefix("Matching locations not found"), cause)
        }
    }

    private suspend fun getMatchingLocation(latitude: Double, longitude: Double) = withContext(dispatcher) {
        try {
            parseMatchingLocation(locationsApi.getLocation(latitude = latitude, longitude = longitude))
        } catch (cause: Exception) {
            throw badRequest(logPrefix("Matching location not found"), cause)
        }
    }

    private fun parseMatchingLocations(matchingLocations: Response<List<MatchingLocationNetwork>>) =
        try {
            Result.success(apiParser.parseOrThrowError(matchingLocations))
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible parse matching locations from network"), cause))
        }

    private fun parseMatchingLocation(matchingLocation: Response<MatchingLocationNetwork>) =
        try {
            Result.success(apiParser.parseOrThrowError(matchingLocation))
        } catch (cause: Exception) {
            Result.failure(operationFailed(logPrefix("Impossible parse matching location from network"), cause))
        }

    private suspend fun insertMatchingLocations(locations: List<MatchingLocationEntity>) = withContext(dispatcher) {
        try {
            database.locationsDao().insertMatchingLocations(locations)
        } catch (cause: Exception) {
            throw operationFailed(logPrefix("Impossible add matching locations to storage"), cause)
        }
    }

    private suspend fun insertMatchingLocation(location: MatchingLocationEntity) = withContext(dispatcher) {
        try {
            database.locationsDao().insertMatchingLocation(location)
        } catch (cause: Exception) {
            throw operationFailed(logPrefix("Impossible add matching location to storage"), cause)
        }
    }
}