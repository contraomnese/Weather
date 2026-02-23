package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.operationFailed
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

fun interface UpdateFavoritesForecastsUseCase {
    suspend operator fun invoke(request: List<Int>): Result<List<Int>>
}

class UpdateFavoritesForecastsUseCaseImpl(
    private val repository: ForecastRepository,
) : UpdateFavoritesForecastsUseCase {

    override suspend fun invoke(request: List<Int>) = coroutineScope {
        val deferredResults = request.map { id ->
            async(Dispatchers.IO) {
                updateSingleLocation(id)
            }
        }
        val results = deferredResults.awaitAll()
        val successfulUpdates = results.filter { it.isSuccess }
        if (successfulUpdates.isEmpty()) {
            Result.failure(operationFailed(logPrefix("Forecast refreshing not success")))
        } else {
            Result.success(successfulUpdates.map { it.getOrThrow() })
        }
    }

    private suspend fun updateSingleLocation(id: Int): Result<Int> {
        return try {
            repository.refreshForecastByLocationId(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}