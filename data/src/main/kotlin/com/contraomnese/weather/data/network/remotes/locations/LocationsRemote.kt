package com.contraomnese.weather.data.network.remotes.locations

import com.contraomnese.weather.data.storage.db.locations.entities.MatchingLocationEntity

interface LocationsRemote {
    suspend fun fetchLocations(query: String): Result<List<MatchingLocationEntity>>
}