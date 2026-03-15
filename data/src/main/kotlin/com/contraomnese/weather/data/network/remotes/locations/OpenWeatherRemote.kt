package com.contraomnese.weather.data.network.remotes.locations

import com.contraomnese.weather.data.mappers.locations.toEntity
import com.contraomnese.weather.data.network.api.OpenWeatherGeoCodingApi
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

class OpenWeatherRemote(
    private val api: OpenWeatherGeoCodingApi,
    private val parser: INetworkParser,
) : LocationsRemote {

    override suspend fun fetchLocations(query: String): Result<List<MatchingLocationEntity>> {
        val response = api.getLocations(query)
        return try {
            Result.success(parser.parseOrThrowError(response).results.map { it.toEntity() })
        } catch (cause: Exception) {
            Result.failure(cause)
        }
    }

}