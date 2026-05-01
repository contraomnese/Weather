package com.contraomnese.weather.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import com.contraomnese.weather.MainActivity
import com.contraomnese.weather.core.ui.utils.getConditionResId
import com.contraomnese.weather.core.ui.utils.getResources
import com.contraomnese.weather.design.R
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

internal const val CHANNEL_ID = "CNTROMNES_WEATHER_CHANNEL_ID"

fun createNewWeatherForecastNotification(
    context: Context,
    locationName: String,
    maxTemperature: String,
    minTemperature: String,
    feelsLikeTemperature: String,
    condition: WeatherCondition,
): Notification {

    val pendingIntent: PendingIntent =
        Intent(context, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(
                context, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    return buildNewWeatherForecastNotification(
        context,
        pendingIntent,
        locationName,
        maxTemperature = maxTemperature,
        minTemperature = minTemperature,
        feelsLikeTemperature = feelsLikeTemperature,
        condition = condition
    )

}

fun buildNewWeatherForecastNotification(
    context: Context,
    pendingIntent: PendingIntent,
    locationName: String,
    maxTemperature: String,
    minTemperature: String,
    feelsLikeTemperature: String,
    condition: WeatherCondition,
): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.notification_channel_title)
        val descriptionText = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(context, CHANNEL_ID)
            .setContentTitle(locationName)
            .setContentText(
                context.getString(
                    R.string.feels_like_temperature_title,
                    feelsLikeTemperature,
                    maxTemperature,
                    minTemperature
                )
            )
            .setStyle(
                Notification
                    .BigTextStyle()
                    .bigText(context.getText(condition.getConditionResId()))
            )
            .setSmallIcon(Icon.createWithResource(context, R.mipmap.ic_launcher_round))
            .setLargeIcon(Icon.createWithResource(context, condition.getResources().iconResId))
            .setContentIntent(pendingIntent)
            .build()
    } else {
        Notification.Builder(context)
            .setContentTitle(locationName)
            .setContentText(
                context.getString(
                    R.string.feels_like_temperature_title,
                    feelsLikeTemperature,
                    maxTemperature,
                    minTemperature
                )
            )
            .setStyle(
                Notification
                    .BigTextStyle()
                    .bigText(context.getText(condition.getConditionResId()))
            )
            .setSmallIcon(Icon.createWithResource(context, R.mipmap.ic_launcher_round))
            .setLargeIcon(Icon.createWithResource(context, condition.getResources().iconResId))
            .setContentIntent(pendingIntent)
            .build()
    }
    return notification
}