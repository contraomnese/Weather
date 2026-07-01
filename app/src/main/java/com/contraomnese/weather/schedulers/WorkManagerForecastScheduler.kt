package com.contraomnese.weather.schedulers

import androidx.work.WorkManager
import com.contraomnese.weather.domain.scheduler.ForecastUpdateScheduler
import com.contraomnese.weather.workers.FavoritesForecastUpdateWorker

class WorkManagerForecastScheduler(
    private val workManager: WorkManager,
) : ForecastUpdateScheduler {

    override fun scheduleFavoritesUpdate(intervalMs: Long, initialDelayMs: Long) {
        FavoritesForecastUpdateWorker.enqueue(workManager, intervalMs, initialDelayMs)
    }

    override fun stopFavoritesUpdate() {
        FavoritesForecastUpdateWorker.stop(workManager)
    }

}