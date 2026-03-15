package com.contraomnese.weather.data.network.remotes.locations

import com.contraomnese.weather.data.mappers.locations.toEntities
import com.contraomnese.weather.data.network.api.LocationsIQApi
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

class LocationIQRemote(
    private val api: LocationsIQApi,
    private val parser: INetworkParser,
) : LocationsRemote {
    override suspend fun fetchLocations(query: String): Result<List<MatchingLocationEntity>> {
        val response = api.getLocations(query)
        return try {
            Result.success(parser.parseOrThrowError(response).toEntities())
        } catch (cause: Exception) {
            Result.failure(cause)
        }
    }
}