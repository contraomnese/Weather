package com.contraomnese.weather.workers

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.contraomnese.weather.domain.app.usecase.GetFavoritesForecastPushNotificationRunningUseCase
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveFavoriteForecastUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateFavoritesForecastsUseCase
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

private const val WORKER_TAG = "CONTRAOMNESE.FORECAST.UPDATE.WORKER"

class FavoritesForecastUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val updateFavoritesForecastsUseCase: UpdateFavoritesForecastsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val observeFavoriteForecastUseCase: ObserveFavoriteForecastUseCase,
    private val getFavoritesForecastPushNotificationRunningUseCase: GetFavoritesForecastPushNotificationRunningUseCase,
) :
    CoroutineWorker(appContext, workerParams) {

    private val notificationManager =
        appContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        return try {
            getFavoritesUseCase()
                .onSuccess {
                    val updatedFavorites = updateFavoritesForecastsUseCase(
                        it.map { favorite ->
                            favorite.id
                        }
                    )

                    updatedFavorites.onEachIndexed { idx, result ->
                        result
                            .onSuccess { id ->
                                if (idx == 0) {
                                    pushNotification(id)
                                }
                            }
                    }
                }
                .onFailure {
                    Result.failure()
                }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun pushNotification(id: Int) {
        getFavoritesForecastPushNotificationRunningUseCase()
            .onSuccess { enabled ->
                if (enabled) {
                    val notificationForecast = observeFavoriteForecastUseCase(id).first()
                    notificationForecast?.let { forecast ->
                        notificationManager.notify(
                            1, createNewWeatherForecastNotification(
                                applicationContext,
                                forecast.locationName,
                                forecast.maxTemperature,
                                forecast.minTemperature,
                                forecast.temperature,
                                forecast.condition
                            )
                        )
                    }
                }
            }
    }

    companion object {

        internal fun enqueue(workManager: WorkManager, interval: Long) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val forecastRequest = PeriodicWorkRequestBuilder<FavoritesForecastUpdateWorker>(
                TimeUnit.MILLISECONDS.toHours(interval),
                TimeUnit.HOURS,
            )
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "ForecastUpdateWork",
                ExistingPeriodicWorkPolicy.UPDATE,
                forecastRequest
            )
        }

        fun stop(workManager: WorkManager) {
            workManager.cancelAllWorkByTag(WORKER_TAG)
        }

    }
}

private fun testNotification(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<FavoritesForecastUpdateWorker>()
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}