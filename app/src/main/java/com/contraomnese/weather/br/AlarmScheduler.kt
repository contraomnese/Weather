package com.contraomnese.weather.br

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

private const val ALARM_REQUEST_CODE = 1002

@SuppressLint("UnspecifiedImmutableFlag")
class AlarmScheduler(private val context: Context) {

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val _intent = Intent(context, FavoritesForecastUpdateReceiver::class.java)

    private val alarmIntent: PendingIntent
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            PendingIntent.getBroadcast(
                context,
                ALARM_REQUEST_CODE,
                _intent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                ALARM_REQUEST_CODE,
                _intent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }

    fun schedule(selectedTimer: Long) {
        if (!isAlarmAlreadySet(context)) {
            Log.d("WEATHER_DEBUG", "schedule $selectedTimer")
            val scheduleTimer = System.currentTimeMillis() + selectedTimer
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                scheduleTimer,
                alarmIntent
            )
        } else {
            Log.d("WEATHER_DEBUG", "scheduler already set")
        }
    }

    fun stop() {
        alarmManager.cancel(alarmIntent)
        alarmIntent.cancel()
        Log.d("AlarmScheduler", "STOPING")
    }

    private fun isAlarmAlreadySet(context: Context): Boolean {
        val intent = Intent(context, AlarmScheduler::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        return pendingIntent != null
    }
}