package com.contraomnese.weather.workers

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateFavoritesForecastsUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit

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

internal fun setupWeatherSync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    val weatherRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(
        6, TimeUnit.HOURS
    )
        .setInitialDelay(getDelayUntilMidnight(), TimeUnit.SECONDS)
        .setConstraints(constraints)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "WeatherUpdateWork",
        ExistingPeriodicWorkPolicy.KEEP,
        weatherRequest
    )
}

internal fun getDelayUntilMidnight(): Long {
    val now = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()

    val today = now.toLocalDateTime(timeZone).date
    val tomorrowMidnight = today
        .plus(1, DateTimeUnit.DAY)
        .atStartOfDayIn(timeZone)
    return (tomorrowMidnight - now).inWholeSeconds
}