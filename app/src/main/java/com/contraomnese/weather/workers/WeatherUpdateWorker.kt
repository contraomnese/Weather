package com.contraomnese.weather.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateFavoritesForecastsUseCase

class WeatherUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val updateFavoritesForecastsUseCase: UpdateFavoritesForecastsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            getFavoritesUseCase()
                .onSuccess {
                    Log.d("WeatherUpdateWorker", "Start update weather")
                    updateFavoritesForecastsUseCase(it.map { it.id })
                }
                .onFailure {
                    Result.failure()
                }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}