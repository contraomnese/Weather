package com.contraomnese.weather.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.contraomnese.weather.data.storage.memory.store.FAVORITES_FORECAST_UPDATE_TIME_EXTRA_NAME
import com.contraomnese.weather.data.storage.memory.store.STOP_FAVORITES_FORECAST_UPDATE_EXTRA_NAME
import com.contraomnese.weather.workers.FavoritesForecastUpdateWorker
import java.util.concurrent.TimeUnit

class FavoritesForecastUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        val stopFavoritesForecastUpdate = intent.getBooleanExtra(
            STOP_FAVORITES_FORECAST_UPDATE_EXTRA_NAME,
            false
        )
        if (stopFavoritesForecastUpdate) {
            FavoritesForecastUpdateWorker.stop(WorkManager.getInstance(context))
            return
        }
        val time = intent.getLongExtra(
            FAVORITES_FORECAST_UPDATE_TIME_EXTRA_NAME,
            TimeUnit.HOURS.toMillis(4)
        )

        FavoritesForecastUpdateWorker.enqueue(WorkManager.getInstance(context), time)
    }
}