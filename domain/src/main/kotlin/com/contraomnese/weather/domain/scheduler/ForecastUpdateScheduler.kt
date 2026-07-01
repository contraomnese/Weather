package com.contraomnese.weather.domain.scheduler

interface ForecastUpdateScheduler {
    fun scheduleFavoritesUpdate(intervalMs: Long, initialDelayMs: Long)
    fun stopFavoritesUpdate()
}